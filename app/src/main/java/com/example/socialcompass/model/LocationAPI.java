package com.example.socialcompass.model;

import android.util.Log;

import androidx.annotation.AnyThread;
import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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

    @WorkerThread
    public Location get(String public_code) {

        var request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/location/" + public_code)
                .method("GET", null)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            Location loc = Location.fromJSON(response.body().string());
            return loc; // null fields if doesn't exist
//            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @AnyThread
    public Future<Location> getAsync(String public_code) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> get(public_code));

        return future;
    }

    @WorkerThread
    public String put(Location loc, String private_code) {

        JsonElement jsonElement = new Gson().toJsonTree(loc);
        JsonObject jsonObject = (JsonObject) jsonElement;
        jsonObject.remove("created_at");
        jsonObject.remove("updated_at");
        jsonObject.remove("public_code");
        jsonObject.addProperty("private_code", private_code);
        String jsonString = jsonObject.toString();

        System.out.println(jsonString);

        var body = RequestBody.create(jsonString, JSON);
        var request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/location/" + loc.public_code)
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
    public Future<String> putAsync(Location loc, String private_code) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> put(loc, private_code));
        return future;
    }

    @WorkerThread
    public String delete(Location loc, String private_code) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("private_code", private_code);
        String jsonString = jsonObject.toString();

        var body = RequestBody.create(jsonString, JSON);
        var request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/location/" + loc.public_code)
                .method("DELETE", body)
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
    public Future<String> deleteAsync(Location loc, String private_code) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> delete(loc, private_code));
        return future;
    }


    @WorkerThread
    public String patch(Location loc, String private_code) {

        JsonElement jsonElement = new Gson().toJsonTree(loc);
        JsonObject jsonObject = (JsonObject) jsonElement;
        jsonObject.remove("created_at");
        jsonObject.remove("updated_at");
        jsonObject.remove("public_code");
        jsonObject.remove("label");
        jsonObject.addProperty("private_code", private_code);
        String jsonString = jsonObject.toString();

        var body = RequestBody.create(jsonString, JSON);
        var request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/location/" + loc.public_code)
                .method("PATCH", body)
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
    public Future<String> patchAsync(Location loc, String private_code) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> patch(loc, private_code));
        return future;
    }
}

