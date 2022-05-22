package in.itsf.quiz.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import in.itsf.quiz.R;
import in.itsf.quiz.helper.AudioHelper;

public class SettingsActivity extends AppCompatActivity {
    public static final int DEFAULT_DIFFICULTY = 1;
    public static final boolean DEFAULT_SOUNDS = true;

    private SharedPreferences sharedPreferences;
    private CheckBox soundCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Create difficulty spinner
        Spinner difficultySpinner = findViewById(R.id.difficultySpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.difficulty_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        difficultySpinner.setAdapter(adapter);
        difficultySpinner.setSelection(sharedPreferences.getInt("difficulty", DEFAULT_DIFFICULTY));
        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("difficulty", i);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Create sounds checkbox
        soundCheckbox = findViewById(R.id.soundCheckbox);
        soundCheckbox.setChecked(sharedPreferences.getBoolean("sounds", DEFAULT_SOUNDS));
        soundCheckbox.setOnClickListener(this::soundChecked);

        // Create settings exit button
        Button exitBtn = findViewById(R.id.settingsExitBtn);
        exitBtn.setOnClickListener(this::exitClicked);
    }

    /**
     * Handle when the sound checkbox is pressed
     */
    public void soundChecked(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("sounds", soundCheckbox.isChecked());
        editor.apply();

        new AudioHelper(this, AudioHelper.Sounds.MENU).play();
    }

    /**
     * Handle when the exit button is pressed
     */
    public void exitClicked(View view) {
        new AudioHelper(this, AudioHelper.Sounds.MENU).play();

        finish();
    }
}