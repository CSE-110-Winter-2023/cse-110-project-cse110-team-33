package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;


public class MainActivity extends AppCompatActivity {

    private OrientationService orientationService;
    private LocationService locationService;
    private AngleCalculation calculator;

    private ImageView compassDisplay;
    private ImageView circle;
    private ImageView red_circle;
    private ImageView yellow_circle;
    private ConstraintLayout compassConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compassDisplay = findViewById(R.id.compassDisplay);
        compassConstraintLayout = findViewById(R.id.compassConstraintLayout);

        //Parent's house
        circle = findViewById(R.id.circle);
        red_circle = findViewById(R.id.red_circle);
        yellow_circle = findViewById(R.id.yellow_circle);
        Pair<Double, Double> parent_house = new Pair<Double, Double>(40.7128, -74.0060);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        //Initialize, pull, and display coordinates
        locationService = LocationService.singleton(this);
        TextView textview = (TextView) findViewById(R.id.locationDisplay);

        locationService.getLocation().observe(this, loc ->{
            textview.setText(Double.toString(loc.first) + " , " + Double.toString(loc.second));
            Log.d("LOCATION", Double.toString(loc.first));
            double relative_angle = calculator.calculateBearing(loc.first, loc.second, parent_house.first, parent_house.second);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) circle.getLayoutParams();
            ConstraintLayout.LayoutParams layoutParams_red = (ConstraintLayout.LayoutParams) red_circle.getLayoutParams();
            ConstraintLayout.LayoutParams layoutParams_yellow = (ConstraintLayout.LayoutParams) yellow_circle.getLayoutParams();

            layoutParams.circleAngle = (float) relative_angle;
            circle.setLayoutParams(layoutParams);
            red_circle.setLayoutParams(layoutParams_red);
            yellow_circle.setLayoutParams(layoutParams_yellow);
        });

        //Initialize, pull, and display orientation
        orientationService = new OrientationService(this);
        TextView orientationDisplay = findViewById(R.id.orientationDisplay);

        orientationService.getOrientation().observe(this, orientation -> {
            //orientationDisplay.setText(Float.toString(orientation));
            orientationDisplay.setText(String.format("%.2f", orientation));
            compassConstraintLayout.setRotation((float) (-orientation*180/3.14159));
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationService.unregisterSensorListeners();
    }

    public void onLaunchChooseBlue(View view) {
        ImageView view_blue = (ImageView) findViewById(R.id.circle);
        if (view_blue.getVisibility() == View.VISIBLE){
            view_blue.setVisibility(View.INVISIBLE);
        } else {
            view_blue.setVisibility(View.VISIBLE);
        }
    }

    public void onLaunchChooseYellow(View view) {
        ImageView view_yellow = (ImageView) findViewById(R.id.yellow_circle);
        if (view_yellow.getVisibility() == View.VISIBLE){
            view_yellow.setVisibility(View.INVISIBLE);
        } else {
            view_yellow.setVisibility(View.VISIBLE);
        }
    }

    public void onLaunchChooseRed(View view) {
        ImageView view_red = (ImageView) findViewById(R.id.red_circle);
        if (view_red.getVisibility() == View.VISIBLE){
            view_red.setVisibility(View.INVISIBLE);
        } else {
            view_red.setVisibility(View.VISIBLE);
        }
    }
}