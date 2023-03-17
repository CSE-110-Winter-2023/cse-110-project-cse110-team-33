package com.example.socialcompass;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.*;

import com.example.socialcompass.activity.MainActivity;
import com.example.socialcompass.utility.DisplayBuilder;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class US15US18InstrumentedTests {
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void US15US18InstrumentedTest() {
        onView(withId(R.id.btnZoomIn)).perform(click());
        onView(withId(R.id.btnZoomOut)).perform(click());
        activityScenarioRule.getScenario().onActivity(activity -> {
            ConstraintLayout constraintLayout = activity.findViewById(R.id.compassConstraintLayout);

            DisplayBuilder uiChecker = activity.getDisplayBuilder();
            assertEquals(2, uiChecker.currentZoomLevel());

        });
    }
}
