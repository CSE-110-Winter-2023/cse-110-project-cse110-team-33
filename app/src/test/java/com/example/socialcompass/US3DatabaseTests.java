package com.example.socialcompass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class US3DatabaseTests {
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
    public void testInsert() {
        Location location1 = new Location("Location 1", 12.23, 23.34,"pink");
        Location location2 = new Location("Location 2", 23.34,12.23, "magenta");

        long id1 = dao.insert(location1);
        long id2 = dao.insert(location2);

        assertNotEquals(id1, id2);
    }

    @Test
    public void testGet() {
        Location insertedlocation = new Location("Bill's house", 124,26.73, "magenta");
        long id = dao.insert(insertedlocation);

        Location location = dao.get(id);
        assertEquals(id, location.id);
        assertEquals(insertedlocation.name, location.name);
        assertEquals(insertedlocation.latitude, location.latitude, 0.1);
        assertEquals(insertedlocation.longitude, location.longitude, 0.1);
        assertEquals(insertedlocation.icon, location.icon);
    }

    @Test
    public void testUpdate() {
        Location location = new Location("Bill's house", 124,26.73, "magenta");
        long id = dao.insert(location);

        location = dao.get(id);
        location.name = "No longer Bill's house";
        int locationsUpdated = dao.update(location);
        assertEquals(1, locationsUpdated);

        location = dao.get(id);
        assertNotNull(location);
        assertEquals("No longer Bill's house", location.name);

    }

    @Test
    public void testDelete() {
        Location location = new Location("Bill's house", 124,26.73, "magenta");
        long id = dao.insert(location);

        location = dao.get(id);
        int locationsDeleted = dao.delete(location);
        assertEquals(1, locationsDeleted);
        assertNull(dao.get(id));

    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }
}

