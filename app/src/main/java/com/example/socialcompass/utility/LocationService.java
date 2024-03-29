package com.example.socialcompass.utility;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.Manifest;

import java.util.Calendar;

public class LocationService implements LocationListener {

    private static LocationService instance;
    private Activity activity;

    private MutableLiveData<Pair<Double, Double>> locationValue;
    private long lastUpdatedAt;

    private final LocationManager locationManager;

    public static LocationService singleton(Activity activity) {
        if (instance == null){
            instance = new LocationService(activity);
        }
        return instance;
    }

    /**
     * Constructor for LocationService
     * @param activity Context needed to initiate LocationManager
     */
    protected LocationService(Activity activity) {
        this.locationValue = new MutableLiveData<>();
        this.activity = activity;
        this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        // Register sensor listeners
        this.registerLocationListener();
        lastUpdatedAt = Calendar.getInstance().getTimeInMillis();
    }

    public void registerLocationListener(){
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            throw new IllegalStateException("App needs location permission to get latest location");
        }

        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.locationValue.postValue(new Pair<Double, Double>(location.getLatitude(),
                location.getLongitude()));
        this.lastUpdatedAt = location.getTime();
    }

    public void unregisterLocationListener() {locationManager.removeUpdates(this); }

    public LiveData<Pair<Double, Double>> getLocation(){ return this.locationValue; }

    public long getUpdatedAt(){ return this.lastUpdatedAt; }

    public void setMockLocationSource(MutableLiveData<Pair<Double, Double>> mockDataSource) {
        unregisterLocationListener();
        this.locationValue = mockDataSource;
    }

    public void mockUpdatedAt(long time){ this.lastUpdatedAt = time; }
}
