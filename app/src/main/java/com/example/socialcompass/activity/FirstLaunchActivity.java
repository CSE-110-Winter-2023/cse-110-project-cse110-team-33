package com.example.socialcompass.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.app.Activity;
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
import com.example.socialcompass.utility.AlertBuilder;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FirstLaunchActivity extends AppCompatActivity {

    EditText displayNameInput;
    EditText publicIDInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);

        displayNameInput = findViewById(R.id.displayNameInput);
        publicIDInput = findViewById(R.id.publicIDInput);
    }

    public boolean validNewPublicID(String public_code) throws ExecutionException, InterruptedException {
        LocationAPI api = LocationAPI.provide();
        Future<Location> result = api.getAsync(public_code, "https://socialcompass.goto.ucsd.edu/location/");
        var toCheck = result.get().public_code;
        if(toCheck == null){
            return true;
        }
        return false;
    }



    public void createUser(View view) throws ExecutionException, InterruptedException {
        String display_name = displayNameInput.getText().toString().trim();
        String public_code = publicIDInput.getText().toString().trim();
        if(!validNewPublicID(public_code)){
            AlertBuilder.showAlert(this, "That Public ID is already taken. Please try again!");
            return;
        }

//        String private_code = UUID.randomUUID().toString();
        String private_code = "0123456789"; // for easier testing

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("public_code", public_code);
        editor.putString("private_code", private_code);
        editor.putString("display_name", display_name);

        editor.apply();

        LocationAPI api = LocationAPI.provide();
        api.putAsync(new Location(public_code, -1, -1,
                display_name), private_code, "https://socialcompass.goto.ucsd.edu/location/");

        // check if profile was created? alert if not

        finish();
    }
}