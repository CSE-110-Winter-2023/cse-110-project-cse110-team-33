package com.example.socialcompass;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "locations")
public class Location {
    @PrimaryKey(autoGenerate = true)
    public long id = 0;

    @NonNull
    public String name;
    public double latitude;
    public double longitude;
    public String icon;

    public Location(@NonNull String name, double latitude, double longitude, String icon) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.icon = icon;
        Log.d("LOCATIONLIST", this.toString());
    }

    public static List<Location> loadJSON(Context context, String path) {
        try {
            InputStream input = context.getAssets().open(path);

            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Location>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", icon='" + icon + '\'' +
                '}';
    }
}