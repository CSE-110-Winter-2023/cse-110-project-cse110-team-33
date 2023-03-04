package com.example.socialcompass.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.socialcompass.model.Location;
import com.example.socialcompass.model.LocationDao;
import com.example.socialcompass.model.LocationDatabase;

import java.util.List;

public class LocationViewModel extends AndroidViewModel {

    private LiveData<List<Location>> locations;
    private final LocationDao locationDao;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        LocationDatabase db = LocationDatabase.getSingleton(context);
        locationDao = db.locationDao();
    }

    public LiveData<List<Location>> getLocationListItems() {
        if (locations == null) {
            loadUsers();
        }
        return locations;
    }

    private void loadUsers() {
        locations = locationDao.getAllLive();
//        Log.d("LOCATIONVM", locations.getValue().toString());
    }

    public void updateName(Location location, String newName) {
        location.public_code = newName;
        locationDao.update(location);
    }

    public void updateLatitude(Location location, Float newLatitude) {
        location.latitude = newLatitude;
        locationDao.update(location);
    }

    public void updateLongitude(Location location, Float newLongitude) {
        location.longitude = newLongitude;
        locationDao.update(location);
    }

    public void updateIcon(Location location, String newIcon) {
        location.label = newIcon;
        locationDao.update(location);
    }

    public void createLocation(Location location) {
        int endOfListOrder = locationDao.getOrderForAppend();
        locationDao.insert(location);
    }

    public void deleteLocation(Location location) {
        locationDao.delete(location);
    }
}
