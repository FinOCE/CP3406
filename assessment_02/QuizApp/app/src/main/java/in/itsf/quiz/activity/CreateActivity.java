package in.itsf.quiz.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import in.itsf.quiz.helper.AudioHelper;
import in.itsf.quiz.model.Answer;
import in.itsf.quiz.adapter.CreateQuestionAdapter;
import in.itsf.quiz.helper.DatabaseHelper;
import in.itsf.quiz.model.Question;
import in.itsf.quiz.model.Quiz;
import in.itsf.quiz.R;

public class CreateActivity extends AppCompatActivity {
    private CreateQuestionAdapter adapter;

    private EditText quizTitle;

    private Quiz quiz = null;
    private int quizId; // Separated as well because it is used before quiz construction
    private boolean editing = false;
    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        quizTitle = findViewById(R.id.quizTitle);

        RecyclerView questionsList = findViewById(R.id.questionsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        questionsList.setLayoutManager(layoutManager);
        questionsList.setNestedScrollingEnabled(false);

        // Add swipe action to remove questions
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder vh, @NonNull RecyclerView.ViewHolder t) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getLayoutPosition();
                questions.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        itemTouchHelper.attachToRecyclerView(questionsList);

        // Handle interactions
        findViewById(R.id.addQuestionBtn).setOnClickListener(this::addQuestionClicked);
        findViewById(R.id.publishBtn).setOnClickListener(this::publishClicked);
        findViewById(R.id.deleteBtn).setOnClickListener(this::deleteClicked);

        if (savedInstanceState == null) {
            // Check if editing an existing quiz
            Intent i = getIntent();
            quizId = i.getIntExtra("quizId", -1);
            if (quizId != -1) {
                try {
                    SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);
                    SQLiteDatabase db = databaseHelper.getReadableDatabase();

                    quiz = DatabaseHelper.fetchQuiz(db, quizId, -1);
                    quizId = quiz.id;
                    editing = true;

                    db.close();
                } catch (SQLiteException e) {
                    Toast.makeText(this, "A database error has occurred", Toast.LENGTH_LONG).show();
                    finish();
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            // Create question manager
            if (editing)
                quizTitle.setText(quiz.title);

            questions = editing ? quiz.questions : new ArrayList<>();
            if (!editing)
                questions.add(Question.getEmpty());
        } else {
            // Restoring from instance state
            Gson gson = new Gson();

            String quizJson = savedInstanceState.getString("quiz");
            quiz = gson.fromJson(quizJson, Quiz.class);

            boolean fresh = savedInstanceState.getBoolean("fresh", true); // Check if saveInstanceState call or not
            questions = quiz.questions;
            if (!editing && fresh)
                questions.add(Question.getEmpty());

            editing = savedInstanceState.getBoolean("editing");
            quizId = savedInstanceState.getInt("quizId");
            quizTitle.setText(quiz.title);
        }

        // Add adapter to RecyclerView
        adapter = new CreateQuestionAdapter(questions);
        adapter.setHasStableIds(true);
        questionsList.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Quiz quiz = new Quiz(quizId, quizTitle.getText().toString(), questions, questions.size());

        Gson gson = new Gson();
        String quizJson = gson.toJson(quiz);
        outState.putString("quiz", quizJson);
        outState.putBoolean("editing", editing);
        outState.putBoolean("fresh", false);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Editor")
                .setMessage("Are you sure you want to exit the quiz editor? Changes will not be saved.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Handle when the add question button is pressed
     */
    public void addQuestionClicked(View view) {
        new AudioHelper(this, AudioHelper.Sounds.MENU).play();

        questions.add(Question.getEmpty());
        adapter.notifyItemInserted(questions.size());
    }

    /**
     * Handle when the publish button is pressed
     */
    public void publishClicked(View view) {
        new AudioHelper(this, AudioHelper.Sounds.PUBLISH).play();

        try {
            String title = quizTitle.getText().toString();
            if (title.equals("")) throw new Exception("You need to give the quiz a title");

            for (Question question : questions) {
                if (question.title.equals("")) throw new Exception("A question is missing a title");

                boolean hasValidAnswer = false;
                for (Answer answer : question.answers)
                    if (answer.isCorrect && !answer.text.equals("")) {
                        hasValidAnswer = true;
                        break;
                    }

                if (!hasValidAnswer) throw new Exception("A question has no correct answers");
            }

            // Remove empty answers (done separate so they aren't removed when quiz is still invalid)
            for (Question question : questions)
                for (Answer answer : question.answers)
                    if (answer.text.equals(""))
                        question.answers.remove(answer);

            // Create the quiz in the database
            Random random = new Random();
            Quiz newQuiz = new Quiz(random.nextInt(Integer.MAX_VALUE), title, questions, questions.size());

            try {
                SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);
                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                if (editing)
                    DatabaseHelper.deleteQuiz(db, quizId);

                int newQuizId = DatabaseHelper.insertQuiz(db, newQuiz.title);

                for (Question question : newQuiz.questions) {
                    int questionId = DatabaseHelper.insertQuestion(db, newQuizId, question.title);

                    for (Answer answer : question.answers) {
                        DatabaseHelper.insertAnswer(db, questionId, answer.text, answer.isCorrect);
                    }
                }

                // Start quiz
                Intent intent = new Intent(this, QuizActivity.class);
                intent.putExtra("quizId", newQuizId);
                quizLauncher.launch(intent);
            } catch (SQLiteException e) {
                throw new Exception("A database error has occurred");
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Launch quiz activity so that when exited it leaves the create activity
     */
    ActivityResultLauncher<Intent> quizLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            (ActivityResult result) -> finish()
    );

    /**
     * Handle when the delete button is pressed
     */
    public void deleteClicked(View view) {
        new AudioHelper(this, AudioHelper.Sounds.DELETE).play();

        Context context = this;

        new AlertDialog.Builder(this)
                .setTitle("Delete Quiz")
                .setMessage("Are you sure you want to delete the quiz?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (editing)
                        try {
                            SQLiteOpenHelper databaseHelper = new DatabaseHelper(context);
                            SQLiteDatabase db = databaseHelper.getWritableDatabase();

                            DatabaseHelper.deleteQuiz(db, quizId);

                            db.close();
                        } catch (SQLiteException e) {
                            Toast.makeText(context, "A database error has occurred", Toast.LENGTH_LONG).show();
                        }

                    dialog.dismiss();
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}