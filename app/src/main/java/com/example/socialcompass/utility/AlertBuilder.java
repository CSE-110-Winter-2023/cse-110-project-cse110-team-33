package com.example.socialcompass.utility;

import android.app.Activity;

public class AlertBuilder {
    public static String showAlert(Activity activity, String message) {
        android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(activity);

        alertBuilder
                .setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);

        android.app.AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
        return message;
    }
}
