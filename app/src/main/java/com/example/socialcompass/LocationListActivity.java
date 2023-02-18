package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class LocationListActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public LocationViewModel viewModel;


    private String[] items = new String[]{"blue", "red", "yellow", "green"};

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
        adapter.setOnLatChanged(viewModel::updateLatitude);
        adapter.setOnLongChanged(viewModel::updateLongitude);
        viewModel.getLocationListItems().observe(this, adapter::setLocationList);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void launchExitActivity(View view) {
        finish();
    }



    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.add_location_popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
//        popupView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                popupWindow.dismiss();
//                return true;
//            }
//        });


        Button addBtn = popupView.findViewById(R.id.addBtn);
//        addBtn.setOnClickListener(this::onAddTodoClicked);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText locName = popupView.findViewById(R.id.locName);
                EditText locLat = popupView.findViewById(R.id.locLat);
                EditText locLong = popupView.findViewById(R.id.locLong);
                Spinner locIcon = popupView.findViewById(R.id.locIcon);

                if (locName.getText().toString().isEmpty()) return;
                if (locLat.getText().toString().isEmpty()) return;
                if (locLong.getText().toString().isEmpty()) return;
                if (locIcon.getSelectedItemPosition() < 0) return;

                Location newLocation = new Location(locName.getText().toString(),
                        Double.valueOf(locLat.getText().toString()),
                        Double.valueOf(locLong.getText().toString()),
                        items[locIcon.getSelectedItemPosition()]);
                viewModel.createLocation(newLocation);
                popupWindow.dismiss();
            }
        });
        Spinner locIcon = popupView.findViewById(R.id.locIcon);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(popupView.getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        locIcon.setAdapter(adapter);

    }
}