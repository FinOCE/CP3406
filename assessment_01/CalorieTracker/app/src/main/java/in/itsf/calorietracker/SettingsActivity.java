package in.itsf.calorietracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    public static final int MIN_CALORIES = 1000;
    public static final int MAX_CALORIES = 5000;

    private int initialRequired;
    private String toastWarning;
    private boolean hasCleared = false;

    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Get data from main activity intent
        Intent intent = getIntent();
        initialRequired = intent.getIntExtra("required", Tracker.DEFAULT_REQUIRED);

        // Set text for daily calories
        input = findViewById(R.id.input);
        input.setText(String.format(Locale.getDefault(), "%d", initialRequired));
        input.setOnEditorActionListener(this::inputEditorActioned);

        // Handle click events
        findViewById(R.id.clear).setOnClickListener(this::clearClicked);
        findViewById(R.id.save).setOnClickListener(this::saveClicked);
        findViewById(R.id.cancel).setOnClickListener(this::cancelClicked);
    }

    /**
     * Get the calorie intake filtered to fit expectations
     * @return The filtered caloric intake
     */
    private int getFilteredValue() {
        int value;

        try {
            value = Integer.parseInt(input.getText().toString());
        } catch (Exception e) {
            value = initialRequired;
        }

        if (value < MIN_CALORIES) {
            value = MIN_CALORIES;
            toastWarning = getString(R.string.toast_min);
        }

        if (value > MAX_CALORIES) {
            value = MAX_CALORIES;
            toastWarning = getString(R.string.toast_max);
        }

        return value;
    }

    /**
     * Handle the click event on the clear button
     */
    private void clearClicked(View view) {
        // Build alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(R.string.clear_title);
        builder.setMessage(R.string.clear_message);
        builder.setPositiveButton(getString(R.string.clear_confirm), (DialogInterface dialog, int which) -> {
            Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

            editor.remove("history");
            editor.remove("intake");
            editor.remove("day");

            editor.apply();
            hasCleared = true;

            Toast.makeText(getApplicationContext(), getString(R.string.clear_complete), Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton(getString(R.string.clear_cancel), (DialogInterface dialog, int which) -> {});

        // Show alert
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Handle the click event on the save button
     */
    private void saveClicked(View view) {
        Intent intent = new Intent();
        intent.putExtra("required", getFilteredValue());
        intent.putExtra("toastWarning", toastWarning);
        intent.putExtra("cleared", hasCleared);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Handle the click event on the cancel button
     */
    private void cancelClicked(View view) {
        Intent intent = new Intent();
        intent.putExtra("cleared", hasCleared);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    /**
     * Handle the input being completed
     */
    private boolean inputEditorActioned(TextView view, int actionId, KeyEvent keyEvent) {
        boolean handled = false;

        if (actionId == EditorInfo.IME_ACTION_DONE) {
            // Hide keyboard when value entered
            view.clearFocus();
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

            handled = true;

            // Filter value to meet requirements and notify if invalid
            input.setText(String.format(Locale.getDefault(), "%d", getFilteredValue()));
            if (toastWarning != null)
                Toast.makeText(getApplicationContext(), toastWarning, Toast.LENGTH_SHORT).show();
            toastWarning = null;
        }

        return handled;
    }
}