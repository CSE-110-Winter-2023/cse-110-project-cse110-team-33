package com.example.socialcompass;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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


import java.util.Optional;


import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MainActivity extends AppCompatActivity {

    private OrientationService orientationService;
    private LocationService locationService;
    private AngleCalculation calculator;

    private ImageView compassDisplay;
    private ConstraintLayout compassConstraintLayout;


    private static int REQUEST_CODE_DEP = 24;
    private static int REQUEST_CODE_LLA = 25;

    private List<Location> locationList;
    private Map<Location, ImageView> icons;
    private LocationDao locationDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LocationDatabase db = LocationDatabase.getSingleton(this);
        locationDao = db.locationDao();
        locationList = locationDao.getAll();

        icons = new HashMap<>();

        compassDisplay = findViewById(R.id.compassDisplay);
        compassConstraintLayout = findViewById(R.id.compassConstraintLayout);

        checkLocationPermissions();

        locationService = LocationService.singleton(this);
        updateLocation();

        orientationService = new OrientationService(this);
        updateOrientation();


    }

    private void updateOrientation() {
        TextView orientationDisplay = findViewById(R.id.orientationDisplay);

        orientationService.getOrientation().observe(this, orientation -> {
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
            displayIcons(loc);
            
        });
    }


    private void displayIcons(Pair<Double, Double> loc) {
        for (Location location : locationList) {
            if (!icons.containsKey(location)) {
                ImageView imageView = new ImageView(this);
                imageView.setId(View.generateViewId());
                if (location.icon.equals("blue")) {
                    imageView.setImageResource(R.drawable.circle_blue);
                } else if (location.icon.equals("red")) {
                    imageView.setImageResource(R.drawable.circle_red);
                } else if (location.icon.equals("yellow")) {
                    imageView.setImageResource(R.drawable.circle_yellow);
                } else if (location.icon.equals("green")) {
                    imageView.setImageResource(R.drawable.circle_green);
                } else{
                    imageView.setImageResource(R.drawable.circle_gray);
                }

                ConstraintLayout.LayoutParams newParams = new ConstraintLayout.LayoutParams(88, 88);
                imageView.setLayoutParams(newParams);
                newParams.circleAngle = 0;
                newParams.circleRadius = compassDisplay.getLayoutParams().width/2;
                newParams.circleConstraint = compassDisplay.getId();

                icons.put(location, imageView);
                compassConstraintLayout.addView(imageView);
            }

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) icons.get(location).getLayoutParams();
            double relative_angle = calculator.calculateBearing(loc.first, loc.second, location.longitude, location.latitude);
            params.circleAngle = (float) relative_angle;
            icons.get(location).setLayoutParams(params);

        }
    }

    public Pair<LocationService, OrientationService> getServices() {
        return new Pair<>(locationService, orientationService);
    }

    public void launchLocationListActivity(View view) {
        Intent intent = new Intent(this, LocationListActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LLA);

    }

    public void onGoToDataEntryPage(View view) {
        Intent intent = new Intent(this, DataEntryPage.class);
        startActivityForResult(intent, REQUEST_CODE_DEP);
    }


    @Override
    protected void onPause() {
        super.onPause();
        orientationService.unregisterSensorListeners();
        locationService.unregisterLocationListener();
        for (Map.Entry<Location, ImageView> entry : icons.entrySet()) {
            compassConstraintLayout.removeView(entry.getValue());
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        orientationService.registerSensorListeners();
//        locationService.registerLocationListener();
//        locationList = locationDao.getAll();
//        icons.clear();
//        updateLocation();
//        updateOrientation();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DEP) {
            int orientation = data.getIntExtra("orientation", -1);
            Log.d("MAIN", String.valueOf(orientation));
            if (orientation != -1) {
                MutableLiveData<Float> mockOrientation = new MutableLiveData<>();
                mockOrientation.setValue((float) (((-orientation*Math.PI)/180) % Math.PI));
                orientationService.setMockOrientationService(mockOrientation);
            }
            icons.clear();
            updateLocation();
            updateOrientation();
        }
        else if (requestCode == REQUEST_CODE_LLA){
            //super.onResume();
            orientationService.registerSensorListeners();
            locationService.registerLocationListener();
            locationList = locationDao.getAll();
            icons.clear();
            updateLocation();
            updateOrientation();
        }
    }

}