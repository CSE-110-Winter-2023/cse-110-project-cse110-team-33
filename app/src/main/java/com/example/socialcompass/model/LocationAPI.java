package com.example.socialcompass.model;

import android.util.Log;
import android.util.Pair;

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
    public Location get(String public_code, String MockURL) {
            var request = new Request.Builder()
                    .url(MockURL+ public_code)
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
    public Future<Location> getAsync(String public_code, String MockURL) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> get(public_code, MockURL));

        return future;
    }

    @WorkerThread
    public String put(Location loc, String private_code, String MockURL) {

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
                .url(MockURL + loc.public_code)
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
    public Future<String> putAsync(Location loc, String private_code, String MockURL) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> put(loc, private_code, MockURL));
        return future;
    }

    @WorkerThread
    public String delete(Location loc, String private_code, String MockURL) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("private_code", private_code);
        String jsonString = jsonObject.toString();

        var body = RequestBody.create(jsonString, JSON);
        var request = new Request.Builder()
                .url(MockURL + private_code)
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
    public Future<String> deleteAsync(Location loc, String private_code, String MockURL) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> delete(loc, private_code, MockURL));
        return future;
    }


    @WorkerThread
    public String patch(String public_code, String private_code, Double latitude, Double longitude, String MockURL) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("private_code", private_code);
        jsonObject.addProperty("latitude", latitude);
        jsonObject.addProperty("longitude", longitude);
        String jsonString = jsonObject.toString();

        var body = RequestBody.create(jsonString, JSON);
        var request = new Request.Builder()
                .url(MockURL + public_code)
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
    public Future<String> patchAsync(String public_code, String private_code, Double latitude, Double longitude, String MockURL) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> patch(public_code, private_code, latitude, longitude, MockURL));
        return future;
    }
}

