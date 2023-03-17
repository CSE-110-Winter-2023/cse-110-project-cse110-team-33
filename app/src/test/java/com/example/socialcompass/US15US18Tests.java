package com.example.socialcompass;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.MainActivity;
import com.example.socialcompass.utility.DisplayBuilder;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class US15US18Tests {

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);


    @Test
    public void US15US18StoryTest() {

        var scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            DisplayBuilder uiChecker = activity.getDisplayBuilder();
            uiChecker.zoomIn();
            assertEquals(1, uiChecker.currentZoomLevel());
            uiChecker.zoomOut();
            assertEquals(2, uiChecker.currentZoomLevel());
            uiChecker.zoomOut();
            assertEquals(3, uiChecker.currentZoomLevel());


        });

    }

}
