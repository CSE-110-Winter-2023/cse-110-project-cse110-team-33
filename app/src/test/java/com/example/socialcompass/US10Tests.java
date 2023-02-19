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
//            assertEquals(12,Integer.parseInt(editText.getText().toString()));
        });
        scenario.close();

        var mainscenario = ActivityScenario.launch(MainActivity.class);
        mainscenario.onActivity(activity -> {
            TextView degree = activity.findViewById(R.id.orientationDisplay);
//            System.out.println(textEntered);
//            System.out.println(Integer.parseInt(degree.getText().toString()));
            assertEquals(textEntered,Integer.parseInt(degree.getText().toString()),1);
        });
        mainscenario.close();
    }
}
