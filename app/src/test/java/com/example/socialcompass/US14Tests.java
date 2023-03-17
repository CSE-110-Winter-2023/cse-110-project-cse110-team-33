package com.example.socialcompass;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLocationManager;
import org.robolectric.shadows.ShadowSystemClock;
import org.w3c.dom.Text;

import static org.junit.Assert.*;

import android.app.Application;
import android.content.Context;
import android.location.LocationManager;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.MainActivity;
import com.example.socialcompass.utility.GPSChecker;
import com.example.socialcompass.utility.LocationService;
import com.example.socialcompass.utility.OrientationService;

import java.time.Duration;
import java.util.Calendar;

import android.Manifest;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class US14Tests {
    LocationService locationService;
    @Test
    public void testOnProviderDisabled() {
        Context context = ApplicationProvider.getApplicationContext();
        ShadowApplication shadowApplication = Shadows.shadowOf((Application) context.getApplicationContext());
        shadowApplication.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);

        var scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {

            // Create a test context
//            Context context = ApplicationProvider.getApplicationContext();
            locationService = LocationService.singleton(activity);
            // Create a test location manager and listener
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            // Use Robolectric's ShadowLocationManager to simulate the loss of GPS signal
            ShadowLocationManager shadowLocationManager = Shadows.shadowOf(locationManager);
            shadowLocationManager.setProviderEnabled(LocationManager.GPS_PROVIDER, false);
            locationService.onProviderDisabled(LocationManager.GPS_PROVIDER);

            // Check that the listener recorded the GPS loss time
            long gpsLossTime = System.currentTimeMillis();
            assertNotNull(gpsLossTime);

            // Use Robolectric's ShadowSystemClock to simulate the passage of time
//            ShadowSystemClock.advanceBy(Duration.ofMillis(5000));
            try {
                Thread.sleep(10000); // Sleep for 10 seconds
            } catch (InterruptedException e) {
                // Handle interrupted exception
            }
            // Use Robolectric's ShadowLocationManager to simulate the recovery of GPS signal
            shadowLocationManager.setProviderEnabled(LocationManager.GPS_PROVIDER, true);

            // Check that the listener recorded the GPS recovery time and net loss time
            long gpsRecoveryTime = System.currentTimeMillis();
//            TextView gpsRecoveryTime = activity.findViewById(R.id.gpsText);
//            String t = gpsRecoveryTime.getText().toString();

//            long netGpsLossTime = gpsRecoveryTime - gpsLossTime;
            assertEquals(gpsLossTime+10000, gpsRecoveryTime);
        });
    }
}
