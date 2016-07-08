package com.example.codeprojectandroid.accelerometersample;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccelerometerActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private long lastUpdate;
    private int color = Color.RED;

    RelativeLayout relativeLayout;
    TextView textViewX;
    TextView textViewY;
    TextView textViewZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout1);

        textViewX = (TextView)findViewById(R.id.textViewX);
        textViewY = (TextView)findViewById(R.id.textViewY);
        textViewZ = (TextView)findViewById(R.id.textViewZ);

        // Get the SensorManager instance
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.accelerometer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged (SensorEvent event) {
        DisplayAccelerometerReadings(event);
        CheckShake(event);
    }

    private void DisplayAccelerometerReadings(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        textViewX.setText("X axis reading: " + x);
        textViewY.setText("Y axis reading: " + y);
        textViewZ.setText("Z axis reading: " + z);
    }

    private void CheckShake(SensorEvent event) {

        // Movement
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float accelerationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

        long actualTime = System.currentTimeMillis();

        if (accelerationSquareRoot >= 2) //
        {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            if (color == Color.RED) {
                relativeLayout.setBackgroundColor(Color.YELLOW);
                color = Color.YELLOW;
            } else {
                relativeLayout.setBackgroundColor(Color.RED);
                color = Color.RED;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
