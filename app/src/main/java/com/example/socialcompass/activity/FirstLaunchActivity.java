package com.example.socialcompass.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

import com.example.socialcompass.R;

import java.util.UUID;

public class FirstLaunchActivity extends AppCompatActivity {

    EditText displayNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);

        displayNameInput = findViewById(R.id.displayNameInput);
    }

    public void createUser(View view) {
        String display_name = displayNameInput.getText().toString().trim();
        String public_code = UUID.randomUUID().toString();
        String private_code = UUID.randomUUID().toString();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("public_code", public_code);
        editor.putString("private_code", private_code);
        editor.putString("display_name", display_name);

        editor.apply();

        finish();
    }
}