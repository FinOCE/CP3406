package in.itsf.reflexchecker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start).setOnClickListener(this::startTest);
    }

    public void startTest(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        gameLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> gameLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        (ActivityResult result) -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    long startTime = data.getLongExtra("startTime", 0);
                    long endTime = data.getLongExtra("endTime", 0);
                    float time = TimeUnit.SECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);

                    String text = String.format(Locale.getDefault(), "That took you %d seconds", Math.round(time * 100) / 100);
                    TextView view = (TextView) findViewById(R.id.time);
                    view.setText(text);
                }
            }
        }
    );
}