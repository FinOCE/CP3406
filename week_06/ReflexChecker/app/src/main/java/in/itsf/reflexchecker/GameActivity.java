package in.itsf.reflexchecker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private static final int[] drawables = {
        R.drawable.baseline_favorite_black_48,
        R.drawable.baseline_fingerprint_black_48,
        R.drawable.baseline_flight_takeoff_black_48,
        R.drawable.baseline_language_black_48,
        R.drawable.baseline_question_answer_black_48,
        R.drawable.baseline_star_rate_black_48
    };

    private final Random random = new Random();
    private long startTime;

    private ViewGroup gameRows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameRows = findViewById(R.id.game_rows);

        setupDescription(R.id.task1, R.array.task1_descriptions);
        setupDescription(R.id.task2, R.array.task2_descriptions);

        for (int i = 0; i < 5; i++) {
            addRandomImage();
            addRandomCheckboxes(random.nextInt(2) == 1 ? R.array.fruits : R.array.drinks);
        }

        findViewById(R.id.done).setOnClickListener(this::doneClicked);

        startTime = System.nanoTime();
    }

    private void setupDescription(int taskID, int arrayID) {
        TextView task = findViewById(taskID);
        String[] descriptions = getResources().getStringArray(arrayID);

        int i = random.nextInt(descriptions.length);
        task.setText(descriptions[i]);
    }

    private void addRandomImage() {
        getLayoutInflater().inflate(R.layout.image, gameRows);

        View lastChild = gameRows.getChildAt(gameRows.getChildCount() - 1);
        ImageView image = (ImageView) lastChild;

        int index = random.nextInt(drawables.length);
        image.setImageDrawable(getResources().getDrawableForDensity(drawables[index], 0));
    }

    private void addRandomCheckboxes(int arrayID) {
        getLayoutInflater().inflate(R.layout.checkboxes, gameRows);

        TableRow checkboxParent = (TableRow) gameRows.getChildAt(gameRows.getChildCount() - 1);

        for (int i = 0; i < checkboxParent.getChildCount(); i++) {
            CheckBox checkbox = (CheckBox) checkboxParent.getChildAt(i);

            String[] options = getResources().getStringArray(arrayID);
            int x = random.nextInt(options.length);
            checkbox.setText(options[x]);
            checkbox.setChecked(random.nextInt(2) == 1);
        }
    }

    private void doneClicked(View view) {
        Intent intent = new Intent();
        intent.putExtra("startTime", startTime);
        intent.putExtra("endTime", System.nanoTime());
        setResult(RESULT_OK, intent);
        finish();
    }
}