package com.example.socialcompass.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.socialcompass.R;
import com.example.socialcompass.model.Location;
import com.example.socialcompass.model.LocationAPI;
import com.example.socialcompass.model.LocationDao;
import com.example.socialcompass.model.LocationDatabase;
import com.example.socialcompass.model.LocationRepository;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FirstLaunchActivity extends AppCompatActivity {

    EditText displayNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);

        displayNameInput = findViewById(R.id.displayNameInput);
    }

    public void createUser(View view) throws ExecutionException, InterruptedException {
        String display_name = displayNameInput.getText().toString().trim();
        String public_code = UUID.randomUUID().toString();
        String private_code = UUID.randomUUID().toString();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("public_code", public_code);
        editor.putString("private_code", private_code);
        editor.putString("display_name", display_name);

        editor.apply();

        LocationAPI api = LocationAPI.provide();
        api.putAsync(new Location(public_code, -1, -1,
                display_name), private_code);

        // check if profile was created? alert if not

        finish();
    }
}