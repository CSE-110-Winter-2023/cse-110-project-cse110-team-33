package com.example.socialcompass.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;

import com.example.socialcompass.model.Location;
import com.example.socialcompass.view.LocationAdapter;
import com.example.socialcompass.viewmodel.LocationViewModel;
import com.example.socialcompass.R;

import java.util.Optional;

public class LocationListActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public LocationViewModel viewModel;
    public View popupView;


    private final String[] items = new String[]{"blue", "red", "yellow", "green"};

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
        adapter.setOnIconChanged(viewModel::updateIcon);
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
//        setResult(RESULT_OK, new Intent());


        EditText mock_view_input = findViewById(R.id.mock_input);
        String mock_string_input = mock_view_input.getText().toString();

        Optional<Integer> mock_int_input = parseMock(mock_string_input);

        if (mock_int_input.isPresent()) {
            int mock_value = mock_int_input.get();
            if (checkIfValidInput(mock_value)) {

                setResult(RESULT_OK, new Intent().putExtra("orientation", mock_value));
                finish();

            }
        }
//        finish();
        finish();
    }



    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.add_location_popup, null);

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

    public static String showAlert(Activity activity, String message){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        alertBuilder
                .setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
        return message;
    }

    public static boolean checkIfValidInput(int input){
        if(input < 0 || input > 359){
            return false;
        }
        return true;
    }

    public void onConfirmClicked(View view) {

        EditText mock_view_input = findViewById(R.id.mock_input);
        String mock_string_input = mock_view_input.getText().toString();

        Optional<Integer> mock_int_input = parseMock(mock_string_input);

        if(!mock_int_input.isPresent()){
            LocationListActivity.showAlert(this,"This is not a number");
            return;
        }

        int mock_value = mock_int_input.get();

        if(!checkIfValidInput(mock_value)){
            LocationListActivity.showAlert(this, "Please enter a number between 0 and 359!");
            return;
        }

        setResult(RESULT_OK, new Intent().putExtra("orientation", mock_value));
        finish();

    }
    public static Optional<Integer> parseMock(String str){
        try{
            int maxCount = Integer.parseInt(str);
            return Optional.of(maxCount);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}