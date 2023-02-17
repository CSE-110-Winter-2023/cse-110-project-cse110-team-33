package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;
import android.content.Intent;
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

import java.util.Optional;


public class MainActivity extends AppCompatActivity {

    private OrientationService orientationService;
    private LocationService locationService;
    private AngleCalculation calculator;

    private ImageView compassDisplay;
    private ImageView blueCircle;
    private ImageView redCircle;
    private ImageView yellowCircle;
    private ConstraintLayout compassConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compassDisplay = findViewById(R.id.compassDisplay);
        compassConstraintLayout = findViewById(R.id.compassConstraintLayout);

        //Parent's house
        blueCircle = findViewById(R.id.blueCircle);
        redCircle = findViewById(R.id.redCircle);
        yellowCircle = findViewById(R.id.yellowCircle);
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
            ConstraintLayout.LayoutParams layoutParamsBlue = (ConstraintLayout.LayoutParams) blueCircle.getLayoutParams();
            ConstraintLayout.LayoutParams layoutParamsRed = (ConstraintLayout.LayoutParams) redCircle.getLayoutParams();
            ConstraintLayout.LayoutParams layoutParamsYellow = (ConstraintLayout.LayoutParams) yellowCircle.getLayoutParams();

            layoutParamsBlue.circleAngle = (float) relative_angle;
            layoutParamsRed.circleAngle = (float) relative_angle;
            layoutParamsYellow.circleAngle = (float) relative_angle;
            blueCircle.setLayoutParams(layoutParamsBlue);
            redCircle.setLayoutParams(layoutParamsRed);
            yellowCircle.setLayoutParams(layoutParamsYellow);
        });

        //Initialize, pull, and display orientation
        orientationService = new OrientationService(this);
        TextView orientationDisplay = findViewById(R.id.orientationDisplay);

        //Get mock orientation value from DataEntryPage
        Intent intent = getIntent();
        int mock_value = intent.getIntExtra("mock_value", 0);
        //Parse the type of value into MutableLiveData<Float>
        MutableLiveData<Float> float_mock_value = new MutableLiveData<>((float) mock_value);
        //Call setMockOrientation function
        orientationService.setMockOrientationService(float_mock_value);

        orientationService.getOrientation().observe(this, orientation -> {
            //orientationDisplay.setText(Float.toString(orientation));
            orientationDisplay.setText(String.format("%.2f", orientation));
            compassConstraintLayout.setRotation((float) (-orientation*180/3.14159));
        });


    }

//    public static Optional<Integer> parseMock(String str){
//        try{
//            int maxCount = Integer.parseInt(str);
//            return Optional.of(maxCount);
//        } catch (NumberFormatException e) {
//            return Optional.empty();
//        }
//    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationService.unregisterSensorListeners();
    }

    public void onLaunchChooseBlue(View view) {
        ImageView view_blue = (ImageView) findViewById(R.id.blueCircle);
        ImageView view_yellow = (ImageView) findViewById(R.id.yellowCircle);
        ImageView view_red = (ImageView) findViewById(R.id.redCircle);
        if (view_blue.getVisibility() == View.VISIBLE){
            view_blue.setVisibility(View.INVISIBLE);
        }
        else {
            view_blue.setVisibility(View.VISIBLE);
            view_red.setVisibility(View.INVISIBLE);
            view_yellow.setVisibility(View.INVISIBLE);
        }
    }

    public void onLaunchChooseYellow(View view) {
        ImageView view_yellow = (ImageView) findViewById(R.id.yellowCircle);
        ImageView view_red = (ImageView) findViewById(R.id.redCircle);
        ImageView view_blue = (ImageView) findViewById(R.id.blueCircle);
        if (view_yellow.getVisibility() == View.VISIBLE){
            view_yellow.setVisibility(View.INVISIBLE);
        } else {
            view_yellow.setVisibility(View.VISIBLE);
            view_blue.setVisibility(View.INVISIBLE);
            view_red.setVisibility(View.INVISIBLE);
        }
    }

    public void onLaunchChooseRed(View view) {
        ImageView view_red = (ImageView) findViewById(R.id.redCircle);
        ImageView view_blue = (ImageView) findViewById(R.id.blueCircle);
        ImageView view_yellow = (ImageView) findViewById(R.id.yellowCircle);
        if (view_red.getVisibility() == View.VISIBLE){
            view_red.setVisibility(View.INVISIBLE);
        } else {
            view_red.setVisibility(View.VISIBLE);
            view_yellow.setVisibility(View.INVISIBLE);
            view_blue.setVisibility(View.INVISIBLE);
        }
    }


    public Pair<LocationService, OrientationService> getServices() {
        return new Pair<>(locationService, orientationService);
    }

    public void onGoToDataEntryPage(View view) {
        Intent intent = new Intent(this, DataEntryPage.class);
        startActivity(intent);
    }


}