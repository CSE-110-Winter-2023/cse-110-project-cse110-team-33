package com.example.socialcompass;

import static org.junit.Assert.*;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

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

        List<Location> todos = Location.loadJSON(context, "mock_locations.json");
        LocationDao = testDb.locationDao();
        LocationDao.insertAll(todos);
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

    // TODO: 2/17/23 add new location test 
    // TODO: 2/17/23 edit icon 

//    @Test
//    public void testAddNewLocation() {
//        String newText = "Ensure all tests pass";
//        ActivityScenario<LocationListActivity> scenario
//                = ActivityScenario.launch(LocationListActivity.class);
//        scenario.moveToState(Lifecycle.State.CREATED);
//        scenario.moveToState(Lifecycle.State.STARTED);
//        scenario.moveToState(Lifecycle.State.RESUMED);
//
//        scenario.onActivity(activity -> {
//            List<Location> beforeTodoList = LocationDao.getAll();
//
//            EditText newTodoText = activity.findViewById(R.id.new_todo_text);
//            Button addTodoButton = activity.findViewById(R.id.add_todo_btn);
//
//            newTodoText.setText(newText);
//            addTodoButton.performClick();
//
//            List<Location> afterTodoList = LocationDao.getAll();
//            assertEquals(beforeTodoList.size()+1, afterTodoList.size());
//            assertEquals(newText, afterTodoList.get(afterTodoList.size()-1).name);
//        });
//    }



    @Test
    public void testDeleteTodo() {
        ActivityScenario<LocationListActivity> scenario
                = ActivityScenario.launch(LocationListActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            List<Location> beforeTodoList = LocationDao.getAll();

            RecyclerView recyclerView = activity.recyclerView;
            RecyclerView.ViewHolder firstVH = recyclerView.findViewHolderForAdapterPosition(0);
            assertNotNull(firstVH);
            long id = firstVH.getItemId();

            View removeButton = firstVH.itemView.findViewById(R.id.addBtn);
            removeButton.performClick();

            List<Location> afterTodoList = LocationDao.getAll();
            assertEquals(beforeTodoList.size()-1, afterTodoList.size());

            Location removedLocation = LocationDao.get(id);
            assertNull(removedLocation);
        });
    }

//    @Test
//    public void testCheckOffTodo() {
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
//            boolean originalCompletion = LocationDao.get(id).completed;
//
//            CheckBox completed = firstVH.itemView.findViewById(R.id.completed);
//            completed.performClick();
//
//            Location editedItem = LocationDao.get(id);
//            assertEquals(!originalCompletion, editedItem.completed);
//        });
//    }
}
