package com.example.socialcompass;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLooper;

import static org.junit.Assert.*;

import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.MainActivity;
import com.example.socialcompass.model.Location;
import com.example.socialcompass.utility.DisplayBuilder;
import com.example.socialcompass.utility.GPSChecker;
import com.example.socialcompass.utility.LocationService;
import com.example.socialcompass.utility.OrientationService;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


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
    public void US14StoryTestEnabled() {

        //Pair<Double, Double> selfLocation = new Pair<>(32.71, -117.42);
        //Location testLocation = new Location("test-location", 40.71, -74.06, "test-location");
        //Map<String, TextView> labels = new HashMap<>();


        var scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {

            ConstraintLayout constraintLayout = activity.findViewById(R.id.compassConstraintLayout);

            MutableLiveData<Float> mockOrientation = new MutableLiveData<>();
            mockOrientation.setValue((float) -(Math.PI));
            MutableLiveData<Pair<Double, Double>> mockLocation = new MutableLiveData<>();
            mockLocation.setValue(new Pair<>(32.879244, -117.231125));
            Pair<LocationService, OrientationService> services = activity.getServices();
            services.first.setMockLocationSource(mockLocation);
            services.second.setMockOrientationService(mockOrientation);

            services.first.mockUpdatedAt(Calendar.getInstance().getTimeInMillis());

            TextView gpsText = (TextView) activity.findViewById(R.id.gpsText);
            ImageView gpsImage = (ImageView) activity.findViewById(R.id.gpsImage);
            GPSChecker gpsChecker = new GPSChecker(services.first, gpsText, gpsImage);

            ShadowLooper.pauseMainLooper();
            gpsChecker.runGPSChecker();
            ShadowLooper.runMainLooperOneTask();

            assertEquals("GPS Signal Detected", gpsText.getText().toString());


        });

    }

    @Test
    public void US14StoryTestDisabled() {

        //Pair<Double, Double> selfLocation = new Pair<>(32.71, -117.42);
        //Location testLocation = new Location("test-location", 40.71, -74.06, "test-location");
        //Map<String, TextView> labels = new HashMap<>();


        var scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {

            ConstraintLayout constraintLayout = activity.findViewById(R.id.compassConstraintLayout);

            MutableLiveData<Float> mockOrientation = new MutableLiveData<>();
            mockOrientation.setValue((float) -(Math.PI));
            MutableLiveData<Pair<Double, Double>> mockLocation = new MutableLiveData<>();
            mockLocation.setValue(new Pair<>(32.879244, -117.231125));
            Pair<LocationService, OrientationService> services = activity.getServices();
            services.first.setMockLocationSource(mockLocation);
            services.second.setMockOrientationService(mockOrientation);

            services.first.mockUpdatedAt(Calendar.getInstance().getTimeInMillis() - 10000);

            TextView gpsText = (TextView) activity.findViewById(R.id.gpsText);
            ImageView gpsImage = (ImageView) activity.findViewById(R.id.gpsImage);
            GPSChecker gpsChecker = new GPSChecker(services.first, gpsText, gpsImage);

            ShadowLooper.pauseMainLooper();
            gpsChecker.runGPSChecker();
            ShadowLooper.runMainLooperOneTask();

            long currentTime = Calendar.getInstance().getTimeInMillis();
            long millis = currentTime - services.first.getUpdatedAt();
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

            assertEquals(hms, gpsText.getText().toString());



        });

    }



}
