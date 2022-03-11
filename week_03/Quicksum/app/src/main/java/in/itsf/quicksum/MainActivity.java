package in.itsf.quicksum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public int sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.one).setOnClickListener(this::buttonClicked);
        findViewById(R.id.two).setOnClickListener(this::buttonClicked);
        findViewById(R.id.three).setOnClickListener(this::buttonClicked);
        findViewById(R.id.four).setOnClickListener(this::buttonClicked);
        findViewById(R.id.five).setOnClickListener(this::buttonClicked);
        findViewById(R.id.six).setOnClickListener(this::buttonClicked);
        findViewById(R.id.seven).setOnClickListener(this::buttonClicked);
        findViewById(R.id.eight).setOnClickListener(this::buttonClicked);
        findViewById(R.id.nine).setOnClickListener(this::buttonClicked);
        findViewById(R.id.clear).setOnClickListener(this::clearSum);
    }

    public void buttonClicked(View view) {
        Button button = (Button) view;
        int number = Integer.parseInt(button.getText().toString());
        sum += number;

        TextView textView = findViewById(R.id.sum);
        String result = "" + sum;
        textView.setText(result);
    }

    public void clearSum(View view) {
        sum = 0;

        TextView textView = findViewById(R.id.sum);
        String result = "" + sum;
        textView.setText(result);
    }
}