package in.itsf.foleyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.nature).setOnClickListener(this::buttonClicked);
        findViewById(R.id.animal).setOnClickListener(this::buttonClicked);
        findViewById(R.id.human).setOnClickListener(this::buttonClicked);
        findViewById(R.id.technology).setOnClickListener(this::buttonClicked);
    }

    public void buttonClicked(View view) {
        Intent intent = new Intent(this, FoleyActivity.class);
        intent.putExtra("type", ((Button) view).getText().toString());
        startActivity(intent);
        finish();
    }
}