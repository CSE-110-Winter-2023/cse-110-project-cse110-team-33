package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

//    private SensorManager sensorManager;
//    private float[] geomagmetic;

    private OrientationService orientationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        orientationService = new OrientationService(this);
        TextView orientationDisplay = findViewById(R.id.orientationDisplay);

        orientationService.getOrientation().observe(this, orientation -> {
            orientationDisplay.setText(Float.toString(orientation));
        });

//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
//                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationService.unregisterSensorListeners();
//        sensorManager.unregisterListener(this);
    }

//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            geomagmetic = sensorEvent.values;
//        }
//
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//
//    }
}