package com.example.socialcompass.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.socialcompass.model.Location;
import com.example.socialcompass.model.LocationAPI;
import com.example.socialcompass.model.LocationDatabase;
import com.example.socialcompass.model.LocationRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FriendViewModel extends AndroidViewModel {
    private LiveData<List<Location>> locations;

    private final LocationRepository repo;

    public FriendViewModel(@NonNull Application application) {
        super(application);
        var context = getApplication().getApplicationContext();
        var db = LocationDatabase.getSingleton(context);
        var dao = db.locationDao();
        var api = LocationAPI.provide();
        this.repo = new LocationRepository(dao, api);
    }

    public LiveData<List<Location>> getLocations() {
        if (locations == null) {
            locations = repo.getAllLocal();
        }
        return locations;
    }

    public LiveData<Location> insertNewLocation(String UID) throws ExecutionException, InterruptedException {
        if (!repo.existsLocal(UID)) {
//            var newLocation = new Location(UID, -1, -1, null);
//            repo.upsertLocal(newLocation);
            LiveData<Location> fromRemote = repo.getRemote(UID);
            if (fromRemote == null) return null; // doesn't exist
            repo.upsertLocal(fromRemote.getValue());
        }
        return repo.getLocal(UID);
    }

    // TODO: get from remote

    public void delete(Location location) {
        repo.deleteLocal(location);
    }
}
