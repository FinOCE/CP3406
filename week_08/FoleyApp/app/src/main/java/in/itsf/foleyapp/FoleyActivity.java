package in.itsf.foleyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Locale;

public class FoleyActivity extends AppCompatActivity {
    private SoundCategory soundCategory;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foley);

        audioManager = new AudioManager(this);

        Intent intent = getIntent();
        soundCategory = SoundCategory.valueOf(intent.getStringExtra("type"));
    }

    public enum SoundCategory {
        animal, human, nature, technology
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        String action = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "started";
                break;
            case MotionEvent.ACTION_MOVE:
                action = "moved";
                break;
            case MotionEvent.ACTION_UP:
                action = "ended";
                break;
        }

        Log.i("TouchableActivity", String.format(Locale.getDefault(), "%.2f %.2f %s", x, y, action));

        float width = this.getResources().getDisplayMetrics().widthPixels;
        float height = this.getResources().getDisplayMetrics().heightPixels;

        // Play the sound depending on what quadrant of the screen is pressed
        int i;
        if (x < width/2 && y < height/2)
            i = 0;
        else if (x >= width/2 && y < height/2)
            i = 1;
        else if (x < width/2 && y >= height/2)
            i = 2;
        else
            i = 3;

        if (action.equals("started")) {
            Log.i("Foley", soundCategory.name() + " " + i);
            audioManager.play(soundCategory, i);
        } else if (action.equals("ended")) {
            audioManager.pause();
        }

        return true;
    }
}