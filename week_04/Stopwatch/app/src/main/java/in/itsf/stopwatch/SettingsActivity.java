package in.itsf.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    public static final int SETTINGS_REQUEST = 1;

    private EditText edit;
    private SeekBar bar;

    public int speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        edit = findViewById(R.id.edit);
        bar = findViewById(R.id.bar);
        bar.setMax(15);

        Button done = findViewById(R.id.done);
        done.setOnClickListener(this::doneClicked);

        Intent intent = getIntent();
        speed = intent.getIntExtra("speed", 1000);

        edit.setText(String.format(Locale.getDefault(), "%d", speed));
        bar.setProgress(speed / 100 - 5);

        edit.setOnEditorActionListener((TextView view, int actionId, KeyEvent event) -> {
            boolean handled = false;

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                int value = Integer.parseInt(edit.getText().toString());

                if (value > 2000) {
                    Toast.makeText(
                        getApplicationContext(),
                        "Speed too large!",
                        Toast.LENGTH_SHORT
                    ).show();

                    value = 2000;
                } else if (value < 500) {
                    Toast.makeText(
                        getApplicationContext(),
                        "Speed too short!",
                        Toast.LENGTH_SHORT
                    ).show();

                    value = 500;
                }

                // Setting progress rounds and sets speed
                bar.setProgress(value / 100 - 5);
                edit.setText(String.format(Locale.getDefault(), "%d", speed));

                view.clearFocus();
                hideKeyboard(view);

                handled = true;
            }

            return handled;
        });

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speed = (progress + 5) * 100;
                edit.setText(String.format(Locale.getDefault(), "%d", speed));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void hideKeyboard(View view) {
        // Pulled from here: https://stackoverflow.com/questions/4165414/how-to-hide-soft-keyboard-on-android-after-clicking-outside-edittext
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void doneClicked(View view) {
        Intent intent = new Intent();
        intent.putExtra("speed", speed);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}