package com.example.socialcompass.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.socialcompass.model.Location;
import com.example.socialcompass.model.LocationAPI;
import com.example.socialcompass.model.LocationDatabase;
import com.example.socialcompass.model.LocationRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LocationViewModel extends AndroidViewModel {

    private final LocationRepository repo;
    private Context context;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        var db = LocationDatabase.getSingleton(context);
        var dao = db.locationDao();
        var api = LocationAPI.provide();
        this.repo = new LocationRepository(dao, api);
    }

    public List<LiveData<Location>> getLocationsLive(List<Location> locationList) {

        List<LiveData<Location>> liveLocationList = new ArrayList<>();

        for (var loc : locationList) {
            LiveData<Location> liveLoc = getLocation(loc.public_code);
            liveLocationList.add(liveLoc);
        }

        return liveLocationList;

    }

    public LiveData<Location> getLocation(String public_code) {
        try {
            LiveData<Location> location = repo.getSynced(public_code);
            return location;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
