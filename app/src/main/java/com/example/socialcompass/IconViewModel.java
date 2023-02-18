package com.example.socialcompass;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class IconViewModel extends AndroidViewModel {

    private List<Location> locationList;
    private final LocationDao locationDao;

    public IconViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        LocationDatabase db = LocationDatabase.getSingleton(context);
        locationDao = db.locationDao();
        locationList = locationDao.getAll();
    }

    public List<Location> getLocationList() {
        locationList = locationDao.getAll();
        return locationList;
    }
}
