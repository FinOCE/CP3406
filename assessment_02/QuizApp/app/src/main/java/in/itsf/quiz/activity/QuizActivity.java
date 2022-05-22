package in.itsf.quiz.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

import in.itsf.quiz.helper.AudioHelper;
import in.itsf.quiz.helper.DatabaseHelper;
import in.itsf.quiz.model.Question;
import in.itsf.quiz.adapter.QuizAnswerAdapter;
import in.itsf.quiz.listener.QuizAnswerClickListener;
import in.itsf.quiz.model.QuizGame;
import in.itsf.quiz.R;

public class QuizActivity extends AppCompatActivity {
    private QuizGame quizGame;
    private QuizAnswerClickListener listener;

    private TextView timerText;
    private TextView quizQuestionText;
    private RecyclerView quizAnswerList;

    private int difficulty;
    private boolean quizCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        timerText = findViewById(R.id.timerText);
        quizQuestionText = findViewById(R.id.quizQuestionText);

        quizAnswerList = findViewById(R.id.quizAnswerList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        quizAnswerList.setLayoutManager(layoutManager);
        quizAnswerList.setNestedScrollingEnabled(false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        difficulty = sharedPreferences.getInt("difficulty", SettingsActivity.DEFAULT_DIFFICULTY);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            int quizId = intent.getIntExtra("quizId", -1);
            int length = intent.getIntExtra("length", -1);

            try {
                SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);
                SQLiteDatabase db = databaseHelper.getReadableDatabase();

                quizGame = new QuizGame(DatabaseHelper.fetchQuiz(db, quizId, length));

                db.close();
                nextQuestion(); // Only occurs if the quiz is successfully applied
            } catch (SQLiteException e) {
                Toast.makeText(this, "A database error has occurred", Toast.LENGTH_LONG).show();
                finish();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            Gson gson = new Gson();
            String json = savedInstanceState.getString("quizGame");
            quizGame = gson.fromJson(json, QuizGame.class);

            quizCompleted = savedInstanceState.getBoolean("quizCompleted");

            if (quizCompleted)
                findViewById(R.id.quizActivity).post(this::finishQuiz);
            else
                nextQuestion(quizGame.currentQuestionTimeLeft);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Quiz")
                .setMessage("Are you sure you want to exit the quiz?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Gson gson = new Gson();
        String json = gson.toJson(quizGame);
        outState.putString("quizGame", json);

        outState.putBoolean("quizCompleted", quizCompleted);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (quizCompleted) {
            try {
                SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);
                SQLiteDatabase db = databaseHelper.getReadableDatabase();

                DatabaseHelper.insertScore(db, quizGame.quiz.id, (int) (quizGame.score * 100f));

                db.close();
            } catch (SQLiteException e) {
                Toast.makeText(this, "A database error occurred", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Display the next question for the quiz
     */
    private void nextQuestion() {
        nextQuestion(-1);
    }

    private void nextQuestion(float duration) {
        boolean reloaded = duration != -1;

        if (!reloaded && quizGame.currentQuestion == quizGame.quiz.questions.size() - 1) {
            finishQuiz();
            return;
        }

        Question question = reloaded ? quizGame.quiz.questions.get(quizGame.currentQuestion) : quizGame.nextQuestion(difficulty);
        final int currentQuestion = quizGame.currentQuestion;
        startTimer(currentQuestion);

        quizQuestionText.setText(question.title);

        // Add new adapter to view
        QuizAnswerAdapter adapter = new QuizAnswerAdapter(question.answers);
        adapter.setHasStableIds(true);
        quizAnswerList.setAdapter(adapter);

        // Replace listener for new question
        quizAnswerList.removeOnItemTouchListener(listener);
        listener = new QuizAnswerClickListener(this, (view, position) -> {
            if (question.answers.get(position).isCorrect) {
                new AudioHelper(this, AudioHelper.Sounds.CORRECT).play();

                quizGame.correctlyAnswered[quizGame.currentQuestion] = true;

                // Update score
                float scoreRatio = quizGame.currentQuestionTimeLeft / quizGame.currentQuestionTime;
                quizGame.score = (quizGame.score * (float) quizGame.currentQuestion + scoreRatio) / ((float) quizGame.currentQuestion + 1f);
            } else {
                new AudioHelper(this, AudioHelper.Sounds.INCORRECT).play();

                quizGame.score = (quizGame.score * (float) quizGame.currentQuestion) / ((float) quizGame.currentQuestion + 1f);
            }
            nextQuestion();
        });
        quizAnswerList.addOnItemTouchListener(listener);
    }

    private void startTimer(int currentQuestion) {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!quizCompleted && quizGame.currentQuestion == currentQuestion) {
                    quizGame.tick();

                    if (quizGame.currentQuestionTimeLeft > 0) {
                        handler.postDelayed(this, (long) (1000f * QuizGame.TICK_DURATION));
                        timerText.setText(quizGame.getTimerText());
                    } else
                        nextQuestion();
                }
            }
        });
    }

    /**
     * Finish the quiz
     */
    private void finishQuiz() {
        quizCompleted = true;

        // Hide all quiz items
        timerText.setText("");
        quizQuestionText.setText("");
        QuizAnswerAdapter adapter = new QuizAnswerAdapter(new ArrayList<>());
        quizAnswerList.setAdapter(adapter);

        // Create popup
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.quiz_complete_popup, null);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        // Set variables and functionality in the popup
        TextView popupNameText = popupView.findViewById(R.id.popupNameText);

        int correctlyAnswered = 0;
        for (boolean correct : quizGame.correctlyAnswered)
            if (correct)
                correctlyAnswered++;

        popupNameText.setText(String.format(
                getResources().getString(R.string.congratulations),
                correctlyAnswered,
                quizGame.quiz.questions.size()
        ));

        TextView popupScoreText = popupView.findViewById(R.id.popupScoreText);
        popupScoreText.setText(String.format(Locale.getDefault(), "%d", (int) (quizGame.score * 100f)));

        popupView.findViewById(R.id.popupShareBtn).setOnClickListener(this::popupShareClicked);
        popupView.findViewById(R.id.popupReturnBtn).setOnClickListener(view -> {
            new AudioHelper(this, AudioHelper.Sounds.MENU).play();
            popupWindow.dismiss();
        });
        popupWindow.setOnDismissListener(this::finish);

        popupWindow.showAtLocation(findViewById(R.id.quizActivity), Gravity.CENTER, 0, 0);
    }

    /**
     * Handle when the share button on the popup is clicked
     */
    public void popupShareClicked(View view) {
        new AudioHelper(this, AudioHelper.Sounds.MENU).play();

        int correctlyAnswered = 0;
        for (boolean correct : quizGame.correctlyAnswered)
            if (correct)
                correctlyAnswered++;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        shareIntent.putExtra(Intent.EXTRA_TEXT, String.format(
                getString(R.string.share_text),
                correctlyAnswered,
                quizGame.quiz.questions.size(),
                getString(R.string.app_name)
        ));

        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_subject)));
    }
}