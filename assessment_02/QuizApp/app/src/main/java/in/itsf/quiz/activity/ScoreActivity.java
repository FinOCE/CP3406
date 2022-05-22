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

import in.itsf.quiz.R;
import in.itsf.quiz.adapter.QuizScoreAdapter;
import in.itsf.quiz.helper.AudioHelper;
import in.itsf.quiz.helper.DatabaseHelper;
import in.itsf.quiz.model.Score;

public class ScoreActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        List<Score> scores = new ArrayList<>();

        try {
            Intent intent = getIntent();
            int quizId = intent.getIntExtra("quizId", -1);
            if (quizId == -1)
                throw new Exception("Quiz could not be found");

            SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query(
                    "score",
                    new String[]{"_id", "quizId", "value"},
                    "quizId = ?", new String[]{Integer.toString(quizId)},
                    null, null, "value DESC"
            );

            if (cursor.moveToFirst())
                do
                    scores.add(new Score(cursor.getInt(0), cursor.getInt(2)));
                while (cursor.moveToNext());

            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, "A database error occurred", Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }

        RecyclerView quizScoreList = findViewById(R.id.quizScoreList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        quizScoreList.setLayoutManager(layoutManager);
        quizScoreList.setNestedScrollingEnabled(false);

        QuizScoreAdapter adapter = new QuizScoreAdapter(scores);
        adapter.setHasStableIds(true);
        quizScoreList.setAdapter(adapter);

        findViewById(R.id.scoreExitBtn).setOnClickListener(this::exitClicked);
    }

    /**
     * Handle when the exit button is pressed
     */
    public void exitClicked(View view) {
        new AudioHelper(this, AudioHelper.Sounds.MENU).play();

        finish();
    }
}