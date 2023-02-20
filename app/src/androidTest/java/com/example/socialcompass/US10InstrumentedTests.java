//package com.example.socialcompass;
//
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.filters.LargeTest;
//import androidx.test.rule.GrantPermissionRule;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static androidx.test.espresso.action.ViewActions.typeText;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//
//import static org.junit.Assert.*;
//
//import android.util.Pair;
//import android.widget.TextView;
//
//@LargeTest
//@RunWith(AndroidJUnit4.class)
//public class US10InstrumentedTests {
//    @Rule
//    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
//
//    @Rule public ActivityScenarioRule<MainActivity> activityScenarioRule
//            = new ActivityScenarioRule<>(MainActivity.class);
//
//    @Test
//    public void mockDataEntryTest() {
//        onView(withId(R.id.dataEntryPage)).perform(click());
//        onView(withId(R.id.mock_input)).perform(typeText("90"), closeSoftKeyboard());
//        onView(withId(R.id.mock_confirm)).perform(click());
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        activityScenarioRule.getScenario().onActivity(activity -> {
//            ConstraintLayout constraintLayout = activity.findViewById(R.id.compassConstraintLayout);
//            Pair<LocationService, OrientationService> services = activity.getServices();
//            TextView degree = activity.findViewById(R.id.orientationDisplay);
//            System.out.println(degree.getText().toString());
//            assertEquals("90", degree.getText().toString());
////            assertEquals(90, constraintLayout.getRotation(), 0.01);
//
//        });
//
//    }
//}
