package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.Manifest;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private OrientationService orientationService;
    private LocationService locationService;
    private AngleCalculation calculator;

    private ImageView compassDisplay;
    private ImageView blueCircle;
    private ConstraintLayout compassConstraintLayout;

    private Button locationManagerButton;

    private List<Location> locationList;


    public IconViewModel iconViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compassDisplay = findViewById(R.id.compassDisplay);
        compassConstraintLayout = findViewById(R.id.compassConstraintLayout);

        iconViewModel = new ViewModelProvider(this)
                .get(IconViewModel.class);

        locationList = iconViewModel.getLocationList();
        Log.d("LOCLIST", locationList.toString());

        //Parent's house
        blueCircle = findViewById(R.id.blueCircle);
//        Pair<Double, Double> parent_house = new Pair<Double, Double>(40.7128, -74.0060);

        // examples of adding icons
        ImageView imageView = new ImageView(this);
        imageView.setId(View.generateViewId());
        imageView.setImageResource(R.drawable.circle_blue);
        ConstraintLayout.LayoutParams newParams = new ConstraintLayout.LayoutParams(88, 88);
        imageView.setLayoutParams(newParams);
        newParams.circleAngle = 270;
        newParams.circleRadius = compassDisplay.getLayoutParams().width/2;
        newParams.circleConstraint = compassDisplay.getId();
        compassConstraintLayout.addView(imageView);


        ImageView imageView2 = new ImageView(this);
        imageView2.setId(View.generateViewId());
        imageView2.setImageResource(R.drawable.circle_green);
        ConstraintLayout.LayoutParams newParams2 = new ConstraintLayout.LayoutParams(88, 88);
        imageView2.setLayoutParams(newParams2);
        newParams2.circleAngle = 90;
        newParams2.circleRadius = compassDisplay.getLayoutParams().width/2;
        newParams2.circleConstraint = compassDisplay.getId();
        compassConstraintLayout.addView(imageView2);

        Log.d("LOCLIST", String.valueOf(imageView.getLayoutParams() == imageView2.getLayoutParams()));
        Log.d("LOCLIST", String.valueOf(imageView.getId() == imageView2.getId()));
        Log.d("LOCLIST", String.valueOf(imageView.getLayoutParams().width));
        Log.d("LOCLIST", String.valueOf(compassConstraintLayout.getChildCount())); //4


        checkLocationPermissions();

        locationService = LocationService.singleton(this);
        updateLocation();

        orientationService = new OrientationService(this);
        updateOrientation();


    }

    private void updateOrientation() {
        TextView orientationDisplay = findViewById(R.id.orientationDisplay);

        orientationService.getOrientation().observe(this, orientation -> {
            //orientationDisplay.setText(Float.toString(orientation));
            orientationDisplay.setText(String.format("%.2f", orientation));
            compassConstraintLayout.setRotation((float) (-orientation*180/3.14159));
        });
    }

    private void checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }

    private void updateLocation() {
        TextView textview = (TextView) findViewById(R.id.locationDisplay);

        locationService.getLocation().observe(this, loc ->{
            textview.setText(Double.toString(loc.first) + " , " + Double.toString(loc.second));
//            double relative_angle = calculator.calculateBearing(loc.first, loc.second, parent_house.first, parent_house.second);
//            ConstraintLayout.LayoutParams layoutParamsBlue = (ConstraintLayout.LayoutParams) blueCircle.getLayoutParams();
//            layoutParamsBlue.circleAngle = (float) relative_angle;
//            blueCircle.setLayoutParams(layoutParamsBlue);

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationService.unregisterSensorListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        orientationService.registerSensorListeners();
        locationList = iconViewModel.getLocationList();
        Log.d("LOCLIST", locationList.toString());

    }

    public Pair<LocationService, OrientationService> getServices() {
        return new Pair<>(locationService, orientationService);
    }

    public void launchLocationListActivity(View view) {
        Intent intent = new Intent(this, LocationListActivity.class);
        startActivity(intent);
    }
}