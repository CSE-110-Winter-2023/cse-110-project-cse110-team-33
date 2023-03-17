package com.example.socialcompass;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.MainActivity;
import com.example.socialcompass.model.Location;
import com.example.socialcompass.utility.DisplayBuilder;

import java.util.HashMap;
import java.util.Map;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class US16Tests {

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);


    @Test
    public void US16StoryTest() {

        Pair<Double, Double> selfLocation = new Pair<>(32.71, -117.42);
        Location testLocation = new Location("test-location", 40.71, -74.06, "test-location");
        Map<String, TextView> labels = new HashMap<>();


        var scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {

            ConstraintLayout constraintLayout = activity.findViewById(R.id.compassConstraintLayout);
            ImageView img = activity.findViewById(R.id.compassDisplay);

            DisplayBuilder uiChecker = activity.getDisplayBuilder();
            TextView toCheck = uiChecker.mockSetLiveLocations(selfLocation, testLocation, labels, img, constraintLayout);

            assertEquals("X", toCheck.getText().toString());


        });

    }

}
