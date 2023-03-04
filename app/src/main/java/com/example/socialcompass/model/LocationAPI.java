package com.example.socialcompass.model;

import android.util.Log;

import androidx.annotation.AnyThread;
import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;

import com.google.gson.Gson;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class LocationAPI {

    private volatile static LocationAPI instance = null;

    private OkHttpClient client;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    public LocationAPI() {
        this.client = new OkHttpClient();
    }

    public static LocationAPI provide() {
        if (instance == null) {
            instance = new LocationAPI();
        }
        return instance;
    }

    /**
     * An example of sending a GET request to the server.
     *
     * The /echo/{msg} endpoint always just returns {"message": msg}.
     *
     * This method should can be called on a background thread (Android
     * disallows network requests on the main thread).
     */
    @WorkerThread
    public String echo(String msg) {
        // URLs cannot contain spaces, so we replace them with %20.
        String encodedMsg = msg.replace(" ", "%20");

        var request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/echo/" + encodedMsg)
                .method("GET", null)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            var body = response.body().string();
            Log.i("ECHO", body);
            return body;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @AnyThread
    public Future<String> echoAsync(String msg) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> echo(msg));

        // We can use future.get(1, SECONDS) to wait for the result.
        return future;
    }

    @WorkerThread
    public Location get(String public_code) {
        String encodedCode = public_code.replace(" ", "%20");

        var request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/locations/" + encodedCode)
                .method("GET", null)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            Location loc = Location.fromJSON(response.body().string());
            return loc; // null fields if doesn't exist
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @AnyThread
    public Future<Location> getAsync(String title) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> get(title));

        return future;
    }

    @WorkerThread
    public String put(Location loc) {
        String encodedCode = loc.public_code.replace(" ", "%20");
        var body = RequestBody.create(loc.toJSON(), JSON);
        var request = new Request.Builder()
                .url("https://sharednotes.goto.ucsd.edu/notes/" + encodedCode)
                .method("PUT", body)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @AnyThread
    public Future<String> putAsync(Location loc) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> put(loc));
        return future;
    }
}

