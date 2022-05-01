package in.itsf.spiritlevel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor gravitySensor;
    private SpiritLevelView spiritLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        spiritLevel = findViewById(R.id.spiritLevel);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i("MainActivity", String.format(Locale.getDefault(), "%s: %d", sensor.getName(), accuracy));
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        Log.i("MainActivity", String.format(Locale.getDefault(), "x %.2f y %.2f z %.2f", x, y, z));

        spiritLevel.setBubble(x, y);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}