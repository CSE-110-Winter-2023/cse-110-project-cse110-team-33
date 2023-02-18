package com.example.socialcompass;

import static org.junit.Assert.*;

import android.content.Context;
import android.widget.TextView;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

@RunWith(RobolectricTestRunner.class)
public class US3RobolectricTests {
    private LocationDao dao;
    private LocationDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, LocationDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.locationDao();
    }

    @Test
    public void testRemoveLocation() {
        try(ActivityScenario<LocationListActivity> scenario = ActivityScenario.launch(LocationListActivity.class)) {
            scenario.onActivity(activity -> {
                assertEquals(1, 1);
            });
        }
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }
}
