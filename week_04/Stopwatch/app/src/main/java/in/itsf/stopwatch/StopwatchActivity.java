package in.itsf.stopwatch;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StopwatchActivity extends AppCompatActivity {
    private Stopwatch stopwatch;
    private Handler handler;
    private boolean isRunning;
    private TextView display;
    private Button toggle;
    private int speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.timer);
        toggle = findViewById(R.id.toggle);

        if (savedInstanceState == null) {
            stopwatch = new Stopwatch();
            speed = 1000;
        } else {
            stopwatch = new Stopwatch(savedInstanceState.getString("value"));
            boolean running = savedInstanceState.getBoolean("running");
            if (running) {
                enableStopwatch();
                toggle.setText(R.string.stop);
            } else {
                display.setText(stopwatch.toString());
            }

            speed = savedInstanceState.getInt("speed");
        }

        toggle.setOnClickListener(this::toggleClicked);
        findViewById(R.id.reset).setOnClickListener(this::resetClicked);
        findViewById(R.id.settings).setOnClickListener(this::settingsClicked);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("value", stopwatch.toString());
        outState.putBoolean("running", isRunning);
        outState.putInt("speed", speed);
    }

    private void enableStopwatch() {
        isRunning = true;

        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    stopwatch.tick();
                    display.setText(stopwatch.toString());
                    handler.postDelayed(this, speed);
                }
            }
        });
    }

    private void disableStopwatch() {
        isRunning = false;
    }

    public void toggleClicked(View view) {
        if (!isRunning) {
            toggle.setText(R.string.stop);
            enableStopwatch();
        } else {
            toggle.setText(R.string.start);
            disableStopwatch();
        }
    }

    public void resetClicked(View view) {
        disableStopwatch();
        stopwatch = new Stopwatch();
        display.setText(stopwatch.toString());
        toggle.setText(R.string.start);
    }

    public void settingsClicked(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("speed", speed);
        settingsLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> settingsLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        (ActivityResult result) -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null)
                    speed = data.getIntExtra("speed", 1000);
            }
        }
    );
}