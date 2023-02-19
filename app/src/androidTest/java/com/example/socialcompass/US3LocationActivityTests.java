package com.example.socialcompass;

import static org.junit.Assert.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class US3LocationActivityTests {
    LocationDatabase testDb;
    LocationDao LocationDao;

    private static void forceLayout(RecyclerView recyclerView) {
        recyclerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        recyclerView.layout(0,0,1080,2280);
    }

    @Before
    public void resetDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context, LocationDatabase.class)
                .allowMainThreadQueries()
                .build();
        LocationDatabase.injectTestDatabase(testDb);

        List<Location> locations = Location.loadJSON(context, "mock_locations.json");
        LocationDao = testDb.locationDao();
        LocationDao.insertAll(locations);
    }

    @Test
    public void testEditLocationName() {
        String newText = "Ensure all tests pass";
        ActivityScenario<LocationListActivity> scenario
                = ActivityScenario.launch(LocationListActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.recyclerView;
            RecyclerView.ViewHolder firstVH = recyclerView.findViewHolderForAdapterPosition(0);
            assertNotNull(firstVH);
            long id = firstVH.getItemId();

            EditText locName = firstVH.itemView.findViewById(R.id.locName);
            locName.requestFocus();
            locName.setText(newText);
            locName.clearFocus();

            Location editedLocation = LocationDao.get(id);
            assertEquals(newText, editedLocation.name);
        });
    }

    @Test
    public void testAddNewLocation() {
        String newName = "Ensure all tests pass";
        int newLong = -50;
        int newLat = 0;
        String newIcon = "blue";

        ActivityScenario<LocationListActivity> scenario
                = ActivityScenario.launch(LocationListActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            List<Location> locationsBefore = LocationDao.getAll();

            Button addButton = activity.findViewById(R.id.addButton);
            addButton.performClick();

            View popupView = activity.popupView;

            EditText newLocationNameText = popupView.findViewById(R.id.locName);
            EditText newLocationLongText = popupView.findViewById(R.id.locLong);
            EditText newLocationLatText = popupView.findViewById(R.id.locLat);
            Spinner newLocationIconText = popupView.findViewById(R.id.locIcon);

            newLocationNameText.setText(newName);
            newLocationLongText.setText(String.valueOf(newLong));
            newLocationLatText.setText(String.valueOf(newLat));
            newLocationIconText.setSelection(0);

            Button confirmAdd = popupView.findViewById(R.id.addBtn);
            confirmAdd.performClick();

            List<Location> locationsAfter = LocationDao.getAll();

            assertEquals(locationsBefore.size()+1, locationsAfter.size());
            assertEquals(newName, locationsAfter.get(0).name);
        });
    }



    @Test
    public void testDeleteLocation() {
        ActivityScenario<LocationListActivity> scenario
                = ActivityScenario.launch(LocationListActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            List<Location> beforeLocationList = LocationDao.getAll();

            RecyclerView recyclerView = activity.recyclerView;
            RecyclerView.ViewHolder firstVH = recyclerView.findViewHolderForAdapterPosition(0);
            assertNotNull(firstVH);
            long id = firstVH.getItemId();

            View removeButton = firstVH.itemView.findViewById(R.id.addBtn);
            removeButton.performClick();

            List<Location> afterLocationList = LocationDao.getAll();
            assertEquals(beforeLocationList.size()-1, afterLocationList.size());

            Location removedLocation = LocationDao.get(id);
            assertNull(removedLocation);
        });
    }

}
