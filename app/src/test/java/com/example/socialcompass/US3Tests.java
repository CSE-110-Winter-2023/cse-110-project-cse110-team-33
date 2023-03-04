package com.example.socialcompass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.runner.AndroidJUnit4;

import com.example.socialcompass.activity.LocationListActivity;
import com.example.socialcompass.model.Location;
import com.example.socialcompass.model.LocationDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class US3Tests {
    // commenting out while fixing api
//
//    LocationDatabase testDb;
//    com.example.socialcompass.model.LocationDao LocationDao;
//
//    private static void forceLayout(RecyclerView recyclerView) {
//        recyclerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        recyclerView.layout(0,0,1080,2280);
//    }
//
//    @Before
//    public void createDb() {
//        Context context = ApplicationProvider.getApplicationContext();
//        testDb = Room.inMemoryDatabaseBuilder(context, LocationDatabase.class)
//                .allowMainThreadQueries()
//                .build();
//        LocationDao = testDb.locationDao();
//    }
//
//    @Before
//    public void resetDatabase() {
//        Context context = ApplicationProvider.getApplicationContext();
//        testDb = Room.inMemoryDatabaseBuilder(context, LocationDatabase.class)
//                .allowMainThreadQueries()
//                .build();
//        LocationDatabase.injectTestDatabase(testDb);
//
//        List<Location> locations = Location.loadJSON(context, "mock_locations.json");
//        LocationDao = testDb.locationDao();
//        LocationDao.insertAll(locations);
//    }
//
//    @Test
//    public void testInsert() {
//        Location location1 = new Location("Location 1", 12.23, 23.34,"pink");
//        Location location2 = new Location("Location 2", 23.34,12.23, "magenta");
//
//        long id1 = LocationDao.insert(location1);
//        long id2 = LocationDao.insert(location2);
//
//        assertNotEquals(id1, id2);
//    }
//
//    @Test
//    public void testGet() {
//        Location insertedlocation = new Location("Bill's house", 124,26.73, "magenta");
//        long id = LocationDao.insert(insertedlocation);
//
//        Location location = LocationDao.get(id);
//        assertEquals(id, location.id);
//        assertEquals(insertedlocation.public_code, location.public_code);
//        assertEquals(insertedlocation.latitude, location.latitude, 0.1);
//        assertEquals(insertedlocation.longitude, location.longitude, 0.1);
//        assertEquals(insertedlocation.label, location.label);
//    }
//
//    @Test
//    public void testUpdate() {
//        Location location = new Location("Bill's house", 124,26.73, "magenta");
//        long id = LocationDao.insert(location);
//
//        location = LocationDao.get(id);
//        location.public_code = "No longer Bill's house";
//        int locationsUpdated = LocationDao.update(location);
//        assertEquals(1, locationsUpdated);
//
//        location = LocationDao.get(id);
//        assertNotNull(location);
//        assertEquals("No longer Bill's house", location.public_code);
//
//    }
//
//    @Test
//    public void testDelete() {
//        Location location = new Location("Bill's house", 124,26.73, "magenta");
//        long id = LocationDao.insert(location);
//
//        location = LocationDao.get(id);
//        int locationsDeleted = LocationDao.delete(location);
//        assertEquals(1, locationsDeleted);
//        assertNull(LocationDao.get(id));
//
//    }
//
//    @Test
//    public void testEditLocationName() {
//        String newText = "Ensure all tests pass";
//        ActivityScenario<LocationListActivity> scenario
//                = ActivityScenario.launch(LocationListActivity.class);
//        scenario.moveToState(Lifecycle.State.CREATED);
//        scenario.moveToState(Lifecycle.State.STARTED);
//        scenario.moveToState(Lifecycle.State.RESUMED);
//
//        scenario.onActivity(activity -> {
//            RecyclerView recyclerView = activity.recyclerView;
//            RecyclerView.ViewHolder firstVH = recyclerView.findViewHolderForAdapterPosition(0);
//            assertNotNull(firstVH);
//            long id = firstVH.getItemId();
//
//            EditText locName = firstVH.itemView.findViewById(R.id.locName);
//            locName.requestFocus();
//            locName.setText(newText);
//            locName.clearFocus();
//
//            Location editedLocation = LocationDao.get(id);
//            assertEquals(newText, editedLocation.public_code);
//        });
//    }
//
//    @Test
//    public void testAddNewLocation() {
//        String newName = "Ensure all tests pass";
//        int newLong = -50;
//        int newLat = 0;
//        String newIcon = "blue";
//
//        ActivityScenario<LocationListActivity> scenario
//                = ActivityScenario.launch(LocationListActivity.class);
//        scenario.moveToState(Lifecycle.State.CREATED);
//        scenario.moveToState(Lifecycle.State.STARTED);
//        scenario.moveToState(Lifecycle.State.RESUMED);
//
//        scenario.onActivity(activity -> {
//            List<Location> locationsBefore = LocationDao.getAll();
//
//            Button addButton = activity.findViewById(R.id.addButton);
//            addButton.performClick();
//
//            View popupView = activity.popupView;
//
//            EditText newLocationNameText = popupView.findViewById(R.id.locName);
//            EditText newLocationLongText = popupView.findViewById(R.id.locLong);
//            EditText newLocationLatText = popupView.findViewById(R.id.locLat);
//            Spinner newLocationIconText = popupView.findViewById(R.id.locIcon);
//
//            newLocationNameText.setText(newName);
//            newLocationLongText.setText(String.valueOf(newLong));
//            newLocationLatText.setText(String.valueOf(newLat));
//            newLocationIconText.setSelection(0);
//
//            Button confirmAdd = popupView.findViewById(R.id.addBtn);
//            confirmAdd.performClick();
//
//            List<Location> locationsAfter = LocationDao.getAll();
//
//            assertEquals(locationsBefore.size()+1, locationsAfter.size());
//            assertEquals(newName, locationsAfter.get(0).public_code);
//        });
//    }
//
//
//
//    @Test
//    public void testDeleteLocation() {
//        ActivityScenario<LocationListActivity> scenario
//                = ActivityScenario.launch(LocationListActivity.class);
//        scenario.moveToState(Lifecycle.State.CREATED);
//        scenario.moveToState(Lifecycle.State.STARTED);
//        scenario.moveToState(Lifecycle.State.RESUMED);
//
//        scenario.onActivity(activity -> {
//            List<Location> beforeLocationList = LocationDao.getAll();
//
//            RecyclerView recyclerView = activity.recyclerView;
//            RecyclerView.ViewHolder firstVH = recyclerView.findViewHolderForAdapterPosition(0);
//            assertNotNull(firstVH);
//            long id = firstVH.getItemId();
//
//            View removeButton = firstVH.itemView.findViewById(R.id.addBtn);
//            removeButton.performClick();
//
//            List<Location> afterLocationList = LocationDao.getAll();
//            assertEquals(beforeLocationList.size()-1, afterLocationList.size());
//
//            Location removedLocation = LocationDao.get(id);
//            assertNull(removedLocation);
//        });
//    }
//
//    @After
//    public void closeDb() throws IOException {
//        testDb.close();
//    }
}

