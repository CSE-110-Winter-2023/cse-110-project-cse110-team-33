package com.example.socialcompass.activity;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;


import com.example.socialcompass.model.LocationAPI;
import com.example.socialcompass.model.LocationRepository;
import com.example.socialcompass.utility.AngleCalculation;
import com.example.socialcompass.model.Location;
import com.example.socialcompass.model.LocationDao;
import com.example.socialcompass.model.LocationDatabase;
import com.example.socialcompass.utility.DisplayBuilder;
import com.example.socialcompass.utility.DistanceCalculation;
import com.example.socialcompass.utility.LocationService;
import com.example.socialcompass.utility.OrientationService;
import com.example.socialcompass.R;
import com.example.socialcompass.viewmodel.LocationViewModel;


import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private OrientationService orientationService;
    private LocationService locationService;


    private ImageView compassDisplay;
    private ConstraintLayout compassConstraintLayout;


    private static int REQUEST_CODE_DEP = 24;
    private static int REQUEST_CODE_LLA = 25;
    private static int REQUEST_CODE_FLA = 27;
    private LocationDao locationDao;

    private LocationViewModel locationVM;

    private LocationRepository repo;
    private LocationAPI api;
    private Map<String, TextView> labels;

    private AngleCalculation calculator;
    private DistanceCalculation distanceCalculator;
    private String public_code;
    private String private_code;
    private String display_name;

    private List<LiveData<Location>> locationList;

    private DisplayBuilder displayBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUp();

        locationVM = new ViewModelProvider(this).get(LocationViewModel.class);

        labels = new HashMap<>();

        compassDisplay = findViewById(R.id.compassDisplay);
        compassConstraintLayout = findViewById(R.id.compassConstraintLayout);

        displayBuilder = new DisplayBuilder(this);

        // creating radar view with a builder
        ConstraintLayout child = displayBuilder.getConstraintLayout();
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) child.getLayoutParams();
        compassConstraintLayout.addView(child);

        params.height = compassConstraintLayout.getHeight();
        params.width = compassConstraintLayout.getWidth();
        params.setMargins(40, 40, 40,40);
        params.topToTop = compassConstraintLayout.getId();
        params.bottomToBottom = compassConstraintLayout.getId();
        params.startToStart = compassConstraintLayout.getId();
        params.endToEnd = compassConstraintLayout.getId();
        getUID();

        if (public_code.equals("null")) {
            Intent intent = new Intent(this, FirstLaunchActivity.class);
            startActivity(intent);
        }

        TextView gpsText = (TextView) findViewById(R.id.gpsText);
        ImageView gpsImage = (ImageView) findViewById(R.id.gpsImage);

        final Handler handler = new Handler();
        final int delay = 5000; // 1000 milliseconds == 1 second
        handler.postDelayed(new Runnable() {
            public void run() {
                if(isGPSEnabled()){
                    Log.d("GPSSTATUS", "enabled!");
                    gpsText.setText("GPS Signal Detected");
                    gpsImage.setImageResource(R.drawable.circle_green);
                }
                else{
                    long currentTime = Calendar.getInstance().getTimeInMillis();
                    long millis = currentTime - locationService.getUpdatedAt();
                    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

                    Log.d("GPSSTATUS", hms);

                    gpsText.setText(hms);
                    gpsImage.setImageResource(R.drawable.circle_red);
                }
                handler.postDelayed(this, delay);
            }
        }, delay);


    }

    private void setUp() {
        LocationDatabase db = LocationDatabase.getSingleton(this);
        locationDao = db.locationDao();
        api = LocationAPI.provide();
        repo = new LocationRepository(locationDao, api);
        checkLocationPermissions();
        locationService = LocationService.singleton(this);
        orientationService = new OrientationService(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationService.unregisterSensorListeners();
        locationService.unregisterLocationListener();
        for (Map.Entry<String, TextView> entry : labels.entrySet()) {
            compassConstraintLayout.removeView(entry.getValue());
        }
        labels.clear();
        for (var liveLoc : this.locationList) {
            liveLoc.removeObservers(this);
        }
        locationList.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUID();
        orientationService.registerSensorListeners();
        locationService.registerLocationListener();

        getFriendsToTrack();

        updateLocation();
        updateOrientation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FLA) {
            getUID();
        }
        if (requestCode != REQUEST_CODE_LLA) return;

        if (data != null) {
            int orientation = data.getIntExtra("orientation", -1);
            if (orientation != -1) {
                MutableLiveData<Float> mockOrientation = new MutableLiveData<>();
                mockOrientation.setValue((float) (((orientation*Math.PI)/180)));
                orientationService.setMockOrientationService(mockOrientation);

            } else {
                orientationService.registerSensorListeners();
                locationService.registerLocationListener();

            }
        } else {
            orientationService.registerSensorListeners();
            locationService.registerLocationListener();
        }
        labels.clear();
        updateLocation();
        updateOrientation();
    }

    private void getUID() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        public_code = preferences.getString("public_code", "null");
        private_code = preferences.getString("private_code", "null");
        display_name = preferences.getString("display_name", "null");

        TextView displayName = findViewById(R.id.displayName);
        TextView publicCode = findViewById(R.id.publicCode);
        TextView privateCode = findViewById(R.id.privateCode);

        displayName.setText(display_name);
        publicCode.setText(public_code);
        privateCode.setText(private_code);

        Log.d("PUBLICCODE", public_code);
    }

    private void updateOrientation() {
        TextView orientationDisplay = findViewById(R.id.orientationDisplay);

        orientationService.getOrientation().observe(this, orientation -> {
            orientationDisplay.setText(String.format("%.2f", orientation*180/3.14159));
            compassConstraintLayout.setRotation((float) - (orientation*180/3.14159));
        });
    }

    private void updateLocation() {
        TextView textview = (TextView) findViewById(R.id.locationDisplay);
        locationService.getLocation().observe(this, loc ->{
            textview.setText(Double.toString(loc.first) + " , " + Double.toString(loc.second));
            displayIcon(loc);
            // patch location on remote
            repo.upsertRemote(public_code,
                    private_code,
                    loc.first,
                    loc.second);

        });
    }

    private void checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }

    private void getFriendsToTrack() {
        if (this.locationList == null) {
            locationList = Collections.emptyList();
        } else {
            this.locationList.clear();
        }

        var fromLocal = repo.getAllLocal();
        fromLocal.observe(this, listEntity-> {
            fromLocal.removeObservers(this);
            Log.d("UIDSTOTRACK", listEntity.toString());
            this.locationList = locationVM.getLocationsLive(listEntity);
        });
    }

    public void displayIcon(Pair<Double, Double> self_location){

        for (var liveLocation : this.locationList) {
            liveLocation.observe(this, location -> {
                displayBuilder.setLiveLocations(self_location,location,labels,compassDisplay,compassConstraintLayout);
            });
            displayBuilder.viewsOverlap(labels);
        }
    }

    public Pair<LocationService, OrientationService> getServices() {
        return new Pair<>(locationService, orientationService);
    }

    public void launchFriendListActivity(View view) {
        Intent intent = new Intent(this, FriendListActivity.class);
        startActivity(intent);
    }

    public void zoomIn(View view) {
        displayBuilder.zoomIn();
        //locationService.unregisterLocationListener();
        for (Map.Entry<String, TextView> entry : labels.entrySet()) {
            compassConstraintLayout.removeView(entry.getValue());
        }
        labels.clear();
        for (var liveLoc : this.locationList) {
            liveLoc.removeObservers(this);
        }
//        locationList.clear();
//
//        //locationService.registerLocationListener();
//        getFriendsToTrack();
        updateLocation();
    }

    public void zoomOut(View view) {
        displayBuilder.zoomOut();
        //locationService.unregisterLocationListener();
        for (Map.Entry<String, TextView> entry : labels.entrySet()) {
            compassConstraintLayout.removeView(entry.getValue());
        }
        labels.clear();
        for (var liveLoc : this.locationList) {
            liveLoc.removeObservers(this);
        }
//        locationList.clear();

        //locationService.registerLocationListener();
//        getFriendsToTrack();
        updateLocation();

    }


    public boolean isGPSEnabled(){
        long currentTime = Calendar.getInstance().getTimeInMillis();
        return locationService.getUpdatedAt() > currentTime - 5000;
    }
}