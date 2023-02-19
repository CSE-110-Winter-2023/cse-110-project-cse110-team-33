package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;

import java.util.Optional;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowLocationManager;

import static org.junit.Assert.*;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;


@RunWith(RobolectricTestRunner.class)
public class US10Tests {
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    @Test
    public void testUIMockForOrientation() {
        var scenario = ActivityScenario.launch(DataEntryPage.class);
        int textEntered = 12;
        scenario.onActivity(activity -> {
            EditText editText = activity.findViewById(R.id.mock_input);
            editText.setText(String.valueOf(textEntered));
            activity.findViewById(R.id.mock_confirm).performClick();
            assertEquals(12,Integer.parseInt(editText.getText().toString()));
        });
//        scenario.close();
//        try {
//            // Wait for the activity to be in the resumed state
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        var mainscenario = ActivityScenario.launch(MainActivity.class);
        mainscenario.onActivity(activity -> {
            var orientationService = new OrientationService(activity);
            orientationService.getOrientation().observe(activity, orientation -> {
                // Get a reference to the TextView
                TextView degree = activity.findViewById(R.id.orientationDisplay);

                // Calculate the mock value
                int mockValue = activity.getIntent().getIntExtra("mock_value", 0);

                // Set the text of the TextView
                degree.setText(String.format("%.2f", (orientation * 180 / 3.14159 + mockValue)));

                // Check if the value of the TextView is correct
                assertEquals("Orientation Display", degree.getText().toString());
            });
        });

//        mainscenario.close();
    }
}
