package in.itsf.calorietracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Tracker tracker;
    private IntakeHistory history;

    private TextView display;
    private TextView description;
    private SeekBar bar;
    private Button add;
    private LinearLayout meals;

    private int caloriesToAdd = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create intake history
        meals = findViewById(R.id.history);

        // Handle instance state
        if (savedInstanceState == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            // Check if it is a new day to reset
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy:MM:dd", Locale.getDefault());
            String currentDay = dateFormatter.format(new Date());
            String storedDay = preferences.getString("storedDay", null);
            boolean isNewDay = currentDay.equals(storedDay);

            // Reset history and intake if new day
            String historyString = isNewDay ? null : preferences.getString("history", null);
            int intake = isNewDay ? 0 : preferences.getInt("intake", 0);
            int required = preferences.getInt("required", Tracker.DEFAULT_REQUIRED);

            tracker = new Tracker(intake, required);

            if (historyString != null) {
                Meal[] meals = new Gson().fromJson(historyString, Meal[].class);
                history = new IntakeHistory(meals);
                updateHistory(meals.length > 0);
            } else {
                history = new IntakeHistory();
            }
        } else {
            // Setup tracker
            int intake = savedInstanceState.getInt("intake");
            int required = savedInstanceState.getInt("required");
            tracker = new Tracker(intake, required);

            // Setup history
            history = savedInstanceState.getParcelable("history");
            boolean eatenToday = savedInstanceState.getBoolean("eatenToday", false);
            updateHistory(eatenToday);
        }

        // Update display
        display = findViewById(R.id.display);
        display.setText(tracker.toString());
        description = findViewById(R.id.description);
        description.setText(getDescription());

        // Handle action events
        findViewById(R.id.settings).setOnClickListener(this::settingsClicked);

        bar = findViewById(R.id.bar);
        bar.setMax(1500);

        bar.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                barProgressChanged(i);
            }
        });

        add = findViewById(R.id.add);
        add.setOnClickListener(this::addClicked);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("intake", tracker.getCalorieIntake());
        outState.putInt("required", tracker.getRequiredCalories());
        outState.putParcelable("history", history);

        if (history.getMeals().size() > 0)
            outState.putBoolean("eatenToday", true);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Convert meals history from ArrayList to String
        Object[] meals = history.getMeals().toArray();
        String historyString = new Gson().toJson(meals);

        // Save values to preferences
        Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy:MM:dd", Locale.getDefault());
        String day = dateFormatter.format(new Date());

        editor.putString("history", historyString);
        editor.putInt("intake", tracker.getCalorieIntake());
        editor.putInt("required", tracker.getRequiredCalories());
        editor.putString("day", day);

        editor.apply();
    }

    /**
     * Handle the click event on the settings button
     */
    private void settingsClicked(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("required", tracker.getRequiredCalories());
        settingsLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> settingsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            (ActivityResult result) -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        // Update new required caloric intake
                        int required = data.getIntExtra("required", Tracker.DEFAULT_REQUIRED);
                        boolean hasCleared = data.getBooleanExtra("cleared", false);

                        boolean eatenToday = findViewById(R.id.empty).getVisibility() == View.GONE;

                        if (hasCleared) {
                            history.clearHistory();
                            tracker.clearCalories();
                            eatenToday = false;
                        }

                        tracker.setCaloriesRequired(required);
                        display.setText(tracker.toString());
                        description.setText(getDescription());
                        meals.removeAllViews();
                        updateHistory(eatenToday);

                        // Make a toast message if supplied
                        String toastWarning = data.getStringExtra("toastWarning");
                        if (toastWarning != null)
                            Toast.makeText(getApplicationContext(), toastWarning, Toast.LENGTH_SHORT).show();
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    Intent data = result.getData();
                    if (data != null) {
                        // Check if history needs to be cleared
                        boolean hasCleared = data.getBooleanExtra("cleared", false);

                        if (hasCleared) {
                            history.clearHistory();
                            tracker.clearCalories();

                            display.setText(tracker.toString());
                            description.setText(getDescription());
                            meals.removeAllViews();
                            updateHistory(false);
                        }
                    }
                }
            }
    );

    /**
     * Handle the click event on the add button
     */
    private void addClicked(View view) {
        findViewById(R.id.empty).setVisibility(View.GONE);

        // Add meal to history
        Meal meal = new Meal(new Date(), caloriesToAdd);
        history.addMeal(meal);
        addMealToHistoryView(meal);

        // Reset calorie addition bar and button, and update displays
        tracker.addCalories(caloriesToAdd);
        caloriesToAdd = 0;

        bar.setProgress(0);

        add.setEnabled(false);
        add.setText(R.string.add_default);

        display.setText(tracker.toString());
        description.setText(getDescription());
    }

    /**
     * Handle the seek bar being dragged
     */
    private void barProgressChanged(int i) {
        caloriesToAdd = Math.round((float)i / 10) * 10;
        add.setEnabled(caloriesToAdd != 0);
        add.setText(String.format(Locale.getDefault(), "add %d", caloriesToAdd));
    }

    /**
     * Add a meal to the history layout view
     * @param meal The meal to be added to the view
     */
    private void addMealToHistoryView(Meal meal) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String text = dateFormatter.format(meal.getDate().getTime()) + ": " + meal.getCalories() + "kcal";

        TextView entry = new TextView(this);
        entry.setText(text);
        entry.setTextSize(20);
        entry.setPadding(20, 20, 20, 0);
        meals.addView(entry);
    }

    /**
     * Get a description of the current state of the tracker
     * @return A string that describes the tracker state
     */
    private String getDescription() {
        String descriptionText;
        int intake = tracker.getCalorieIntake();
        int required = tracker.getRequiredCalories();

        if (intake == 0)
            descriptionText = getString(R.string.description_default);
        else if (intake > required)
            descriptionText = getString(R.string.description_overflow);
        else
            descriptionText = String.format(
                Locale.getDefault(),
                getString(R.string.description_progress),
                Math.round(((float)intake / (float)required) * 100)
            );

        return descriptionText;
    }

    /**
     * Update the history view of the activity
     */
    private void updateHistory(boolean eatenToday) {
        for (Meal meal : history.getMeals())
            addMealToHistoryView(meal);

        findViewById(R.id.empty).setVisibility(eatenToday ? View.GONE : View.VISIBLE);
    }
}