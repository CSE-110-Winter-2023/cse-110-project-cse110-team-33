package com.example.socialcompass;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowLocationManager;

import static org.junit.Assert.*;

import android.app.Application;
import android.location.LocationManager;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.MainActivity;
import com.example.socialcompass.utility.GPSChecker;
import com.example.socialcompass.utility.LocationService;
import com.example.socialcompass.utility.OrientationService;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class US14Tests {

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);


    @Test
    public void storyTest14() {

        var scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {

            MutableLiveData<Float> mockOrientation = new MutableLiveData<>();
            mockOrientation.setValue((float) -(Math.PI));

            MutableLiveData<Pair<Double, Double>> mockLocation = new MutableLiveData<>();
            mockLocation.setValue(new Pair<>(32.879244, -117.231125));

            Pair<LocationService, OrientationService> services = activity.getServices();

            services.first.setMockLocationSource(mockLocation);
            services.second.setMockOrientationService(mockOrientation);

            TextView gpsTextTest = activity.findViewById(R.id.gpsText);
            ImageView gpsImageTest = activity.findViewById(R.id.gpsImage);

            GPSChecker mockGPSChecker = new GPSChecker(services.first, gpsTextTest, gpsImageTest);
            mockGPSChecker.runGPSChecker();

            assertEquals("No signal", gpsTextTest.getText().toString());

        });





    }

}
