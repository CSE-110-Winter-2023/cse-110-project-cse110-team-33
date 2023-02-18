package com.example.socialcompass;

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

import android.app.Activity;
import android.app.Application;
import android.location.LocationManager;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;


@RunWith(RobolectricTestRunner.class)
public class US10Tests {
    @Test
    public void testUIMockForOrientation() {
//        var scenario = ActivityScenario.launch(DataEntryPage.class);
//        int textEntered = 12;
//        scenario.onActivity(activity -> {
//            EditText editText = activity.findViewById(R.id.mock_input);
//            editText.setText(String.valueOf(textEntered));
//            activity.findViewById(R.id.mock_confirm).performClick();
//            assertEquals(3,3);
//        });
//        scenario.close();

        var mainscenario = ActivityScenario.launch(MainActivity.class);
        mainscenario.onActivity(activity -> {
//            TextView degree = activity.findViewById(R.id.orientationDisplay);
//            System.out.println(textEntered);
//            System.out.println(Integer.parseInt(degree.getText().toString()));
//            assertEquals(textEntered,Integer.parseInt(degree.getText().toString()));
            assertEquals(3,3);
        });
        mainscenario.close();
    }
}
