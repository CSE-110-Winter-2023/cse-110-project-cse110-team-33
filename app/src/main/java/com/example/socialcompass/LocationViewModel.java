package com.example.socialcompass;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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

    public LiveData<List<Location>> getTodoListItems() {
        if (locations == null) {
            loadUsers();
        }
        return locations;
    }

    private void loadUsers() {
        locations = locationDao.getAllLive();
    }

    public void updateName(Location location, String newName) {
        location.name = newName;
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
        location.icon = newIcon;
        locationDao.update(location);
    }

    public void createLocation(String name) {
        int endOfListOrder = locationDao.getOrderForAppend();
        Location newItem = new Location(name, 0, 0, "blue");
        locationDao.insert(newItem);
    }

    public void deleteLocation(Location location) {
        locationDao.delete(location);
    }
}
