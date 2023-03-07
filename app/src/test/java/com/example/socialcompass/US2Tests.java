package com.example.socialcompass;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

import android.util.Pair;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.MainActivity;
import com.example.socialcompass.utility.AngleCalculation;
import com.example.socialcompass.utility.LocationService;
import com.example.socialcompass.utility.OrientationService;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class US2Tests {
    @Test
    public void testCalculateBearing_ForValidInputs() {
        double lat1 = 40.730610;
        double long1 = -73.935242;
        double lat2 = 48.856613;
        double long2 = 2.352222;
        double expectedBearing = 53.737273807858585;

        double actualBearing = AngleCalculation.calculateBearing(lat1, long1, lat2, long2);
        assertEquals(expectedBearing, actualBearing, 0.1);
    }

    @Test
    public void testCalculateBearing_ForBoundaryInputs() {
        double lat1 = 90;
        double long1 = 180;
        double lat2 = -90;
        double long2 = -180;
        double expectedBearing = 180;

        double actualBearing = AngleCalculation.calculateBearing(lat1, long1, lat2, long2);
        assertEquals(expectedBearing, actualBearing, 0.1);
    }

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);


    @Test
    public void testStoryTest3() {

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

            float azimuth1 = services.second.getOrientation().getValue();
            assertTrue(azimuth1 == (float) -(Math.PI));

            Pair<Double, Double> currentLoc1 = services.first.getLocation().getValue();

            double latitude = 40.7128;
            double longitude = -74.0060;
            double expectedBearing = 64.5;
            double actualBearing1 = AngleCalculation.calculateBearing(currentLoc1.first, currentLoc1.second, latitude, longitude);
            assertEquals(expectedBearing, actualBearing1, 0.1);

            mockOrientation.setValue((float) 0);
            float azimuth2 = services.second.getOrientation().getValue();
            assertEquals(0, azimuth2, 0.1);
            Pair<Double, Double> currentLoc2 = services.first.getLocation().getValue();
            double actualBearing2 = AngleCalculation.calculateBearing(currentLoc2.first, currentLoc2.second, latitude, longitude);
            assertEquals(expectedBearing, actualBearing2, 0.1);


        });





    }

}
