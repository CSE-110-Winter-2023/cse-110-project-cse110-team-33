package com.example.socialcompass.model;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

class TimestampAdapter extends TypeAdapter<Long> {

    @Override
    public void write(com.google.gson.stream.JsonWriter out, Long value) throws IOException {
        var instant = Instant.ofEpochSecond(value);
        out.value(instant.toString());

    }

    @Override
    public Long read(com.google.gson.stream.JsonReader in) throws IOException {
        var instant = Instant.parse(in.nextString());
        return instant.getEpochSecond();
    }
}

@Entity(tableName = "locations")
public class Location {
//    @PrimaryKey(autoGenerate = true)
//    public long id = 0;

    @PrimaryKey
    @SerializedName("public_code")
    @NonNull
    public String public_code;

    @NonNull
    public double latitude;
    public double longitude;
    public String label;

    @JsonAdapter(TimestampAdapter.class)
    @SerializedName(value = "created_at", alternate = "createAt")
    public long created_at;

    @JsonAdapter(TimestampAdapter.class)
    @SerializedName(value = "updated_at", alternate = "updatedAt")
    public long updated_at;

    public Location(@NonNull String public_code, double latitude, double longitude, String label) {
        this.public_code = public_code;
        this.latitude = latitude;
        this.longitude = longitude;
        this.label = label;
        this.created_at = 0;
        this.updated_at = 0;
        Log.d("LOCATIONLIST", this.toString());
    }

    @Ignore
    public Location(@NonNull String public_code, double latitude, double longitude, String label, long created_at, long updated_at) {
        this.public_code = public_code;
        this.latitude = latitude;
        this.longitude = longitude;
        this.label = label;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // TODO: fix to work with new fields
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
                "public_code='" + public_code + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", label='" + label + '\'' +
                '}';
    }
}