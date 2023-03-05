package com.example.socialcompass.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LocationRepository {
    private final LocationDao dao;

    private final LocationAPI api;

    public LocationRepository(LocationDao dao, LocationAPI api) {
        this.dao = dao;
        this.api = api;
    }

    // Synced Methods
    // ==============

    public LiveData<Location> getSynced(String public_code) throws ExecutionException, InterruptedException {
//        var loc = new MediatorLiveData<Location>();
//
//        Observer<Location> updateFromRemote = theirLoc -> {
//            var ourLoc = loc.getValue();
//            if (theirLoc == null) return; // do nothing
//            if (ourLoc == null || ourLoc.updated_at < theirLoc.updated_at) {
//                upsertLocal(theirLoc);
//            }
//        };
//
//        // If we get a local update, pass it on.
//        loc.addSource(getLocal(public_code), loc::postValue);
//        // If we get a remote update, update the local version (triggering the above observer)
//        loc.addSource(getRemote(public_code), updateFromRemote);
//
//        return loc;
        return null;
    }

    public void upsertSynced(Location loc) {
//        note.version = note.version + 1;
//        upsertLocal(note);
//        upsertRemote(note);
    }

    // Local Methods
    // =============

    public LiveData<Location> getLocal(String public_code) {
        return dao.get(public_code);
    }

    public LiveData<List<Location>> getAllLocal() {
        return dao.getAll();
    }

    public void upsertLocal(Location loc) {
//        note.version = note.version + 1;
        dao.upsert(loc);
    }

    public void deleteLocal(Location loc) {
        dao.delete(loc);
    }

    public boolean existsLocal(String public_code) {
        return dao.exists(public_code);
    }

    // Remote Methods
    // ==============

    public LiveData<Location> getRemote(String public_code) throws ExecutionException, InterruptedException {
        Location initialLoc = api.getAsync(public_code).get();
        if (initialLoc.public_code == null) return null;

        var location = new MutableLiveData<Location>();
        location.setValue(initialLoc);

        var executor = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(() -> {
            Future<Location> future = api.getAsync(public_code);
            try {
                location.postValue(future.get(1, TimeUnit.SECONDS));
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }
        }, 0, 3, TimeUnit.SECONDS);

        return location;
    }

    public void upsertRemote(double latitude, double longitude, String private_code) {
        Location location = new Location("public_code",
                latitude, longitude, "label");
        api.patchAsync(location, private_code);
    }

}

