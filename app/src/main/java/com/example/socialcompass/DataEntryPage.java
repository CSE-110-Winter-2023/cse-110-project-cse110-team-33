package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Optional;

public class DataEntryPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry_page);
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

        TextView mock_view_input = findViewById(R.id.mock_input);
        String mock_string_input = mock_view_input.getText().toString();

        Optional<Integer> mock_int_input = DataEntryPage.parseMock(mock_string_input);

        if(!mock_int_input.isPresent()){
            String s = DataEntryPage.showAlert(this,"This is not a number");
            return;
        }

        int mock_value = mock_int_input.get();

        if(!checkIfValidInput(mock_value)){
            String s = DataEntryPage.showAlert(this, "Please enter a number between 0 and 359!");
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

    public void GoBackMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}