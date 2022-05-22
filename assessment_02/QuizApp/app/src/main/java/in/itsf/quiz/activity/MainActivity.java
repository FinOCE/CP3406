package in.itsf.quiz.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import in.itsf.quiz.R;
import in.itsf.quiz.helper.AudioHelper;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign activity interactions
        findViewById(R.id.quickStartBtn).setOnClickListener(this::quickStartClicked);
        findViewById(R.id.categorySelectionBtn).setOnClickListener(this::categorySelectionClicked);
        findViewById(R.id.createBtn).setOnClickListener(this::createClicked);
        findViewById(R.id.settingsBtn).setOnClickListener(this::settingsClicked);
    }

    /**
     * Handle when the quick start button is pressed
     */
    public void quickStartClicked(View view) {
        new AudioHelper(this, AudioHelper.Sounds.MENU).play();

        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("quizId", -1);
        startActivity(intent);
    }

    /**
     * Handle when the category selection button is pressed
     */
    public void categorySelectionClicked(View view) {
        new AudioHelper(this, AudioHelper.Sounds.MENU).play();

        Intent intent = new Intent(this, SelectionActivity.class);
        startActivity(intent);
    }

    /**
     * Handle when the create button is pressed
     */
    public void createClicked(View view) {
        new AudioHelper(this, AudioHelper.Sounds.MENU).play();

        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }

    /**
     * Handle when the settings button is pressed
     */
    public void settingsClicked(View view) {
        new AudioHelper(this, AudioHelper.Sounds.MENU).play();

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}