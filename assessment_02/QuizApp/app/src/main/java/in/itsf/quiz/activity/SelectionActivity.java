package in.itsf.quiz.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.itsf.quiz.helper.AudioHelper;
import in.itsf.quiz.helper.DatabaseHelper;
import in.itsf.quiz.model.Question;
import in.itsf.quiz.model.Quiz;
import in.itsf.quiz.R;
import in.itsf.quiz.adapter.SelectQuizAdapter;

public class SelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        // Fetch quizzes from the database
        try {
            SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            // Get all quizzes
            Cursor cursor = db.query("quiz", new String[]{"_id", "title"}, null, null, null, null, null);
            if (!cursor.moveToFirst()) {
                Toast.makeText(this, "No quizzes exist yet, why not try create one?", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            List<Quiz> quizzes = new ArrayList<>();
            do
                quizzes.add(new Quiz(cursor.getInt(0), cursor.getString(1), new ArrayList<>(), 0));
            while (cursor.moveToNext());

            cursor.close();

            // Count questions in each quiz
            cursor = db.query("question", new String[]{"_id", "quizId"}, null, null, null, null, null);
            if (!cursor.moveToFirst()) {
                // This should never occur because of the handling in CreateActivity but is included just in case
                Toast.makeText(this, "Couldn't find any questions, maybe try create a new quiz?", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            do
                for (Quiz quiz : quizzes)
                    if (quiz.id == cursor.getInt(1)) {
                        quiz.addQuestion(Question.getEmpty()); // Actual questions are not necessary, so they can just be added as placeholders for the count
                        break;
                    }
            while (cursor.moveToNext());

            cursor.close();

            // Create quiz manager
            RecyclerView quizList = findViewById(R.id.quizList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            quizList.setLayoutManager(layoutManager);
            quizList.setNestedScrollingEnabled(false);

            // Add adapter to RecyclerView
            SelectQuizAdapter adapter = new SelectQuizAdapter(quizzes);
            adapter.setHasStableIds(true);
            quizList.setAdapter(adapter);

            // Handle when a quiz is clicked
            adapter.setOnClickListener((view, position) -> {
                new AudioHelper(this, AudioHelper.Sounds.MENU).play();

                Quiz quiz = quizzes.get(position);

                Intent intent = new Intent(this, QuizActivity.class);
                intent.putExtra("quizId", quiz.id);
                startActivity(intent);
            });
        } catch (SQLiteException e) {
            Toast.makeText(this, "A database error has occurred!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        findViewById(R.id.selectionExitBtn).setOnClickListener(this::exitClicked);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate(); // Updates the list whenever it is returned to, ensures it is up to date
    }

    /**
     * Handle when the exit button is pressed
     */
    public void exitClicked(View view) {
        new AudioHelper(this, AudioHelper.Sounds.MENU).play();

        finish();
    }
}