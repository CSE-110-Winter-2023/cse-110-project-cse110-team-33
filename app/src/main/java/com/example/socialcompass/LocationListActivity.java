package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class LocationListActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    private LocationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);
        viewModel = new ViewModelProvider(this)
                .get(LocationViewModel.class);

        LocationAdapter adapter = new LocationAdapter();
        adapter.setHasStableIds(true);
        adapter.setOnLocationNameChanged(viewModel::updateName);
        adapter.setOnDeleteBtnClickedHandler(viewModel::deleteLocation);
        viewModel.getTodoListItems().observe(this, adapter::setLocationList);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

//        List<Location> locationList = Location.loadJSON(this, "mock_locations.json");
//        Log.d("LOCATIONLIST", locationList.toString());
//        adapter.setLocationList(locationList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void launchExitActivity(View view) {
        finish();
    }
}