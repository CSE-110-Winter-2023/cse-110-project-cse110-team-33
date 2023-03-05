package com.example.socialcompass.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.socialcompass.R;
import com.example.socialcompass.model.Location;
import com.example.socialcompass.view.FriendAdapter;
import com.example.socialcompass.viewmodel.FriendViewModel;

import java.util.concurrent.ExecutionException;

public class FriendListActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public EditText UIDInput;
    public Button addBtn;

    FriendViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        recyclerView = findViewById(R.id.recyclerView);
        UIDInput = findViewById(R.id.UIDInput);
        addBtn = findViewById(R.id.addBtn);

        // viewmodel
        viewModel = new ViewModelProvider(this).get(FriendViewModel.class);

        // adapter
        var adapter = new FriendAdapter();
        adapter.setHasStableIds(true);
        adapter.setOnLocationDeleteClickedListener(loc ->
                onLocationDeleteClicked(loc, viewModel));
        viewModel.getLocations().observe(this, adapter::setLocations);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }

    private void onLocationDeleteClicked(Location loc, FriendViewModel viewModel) {
        viewModel.delete(loc);
    }


    public void addNewLocation(View view) throws ExecutionException, InterruptedException {
        String UID = UIDInput.getText().toString().trim();
        var newLocation = viewModel.insertNewLocation(UID);
        if (newLocation == null) {
            showAlert(this, "Not a valid UID");
            return;
        }
        newLocation.observe(this, locEntity -> {
            newLocation.removeObservers(this);
            UIDInput.getText().clear();
        });
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
}