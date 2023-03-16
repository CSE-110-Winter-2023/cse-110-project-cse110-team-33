package com.example.socialcompass.utility;

import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.socialcompass.R;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class GPSChecker {
    LocationService locationService;
    TextView gpsText;
    ImageView gpsImage;

    public GPSChecker(LocationService newLocationService, TextView newGpsText, ImageView newGpsImage){

        this.locationService = newLocationService;
        this.gpsText = newGpsText;
        this.gpsImage = newGpsImage;

    }

    public void runGPSChecker(){
        final Handler handler = new Handler();
        final int delay = 5000; // 1000 milliseconds == 1 second
        handler.postDelayed(new Runnable() {
            public void run() {
                if(isGPSEnabled()){
                    Log.d("GPSSTATUS", "enabled!");
                    gpsText.setText("GPS Signal Detected");
                    gpsImage.setImageResource(R.drawable.circle_green);
                }
                else{
                    long currentTime = Calendar.getInstance().getTimeInMillis();
                    long millis = currentTime - locationService.getUpdatedAt();
                    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

                    Log.d("GPSSTATUS", hms);

                    gpsText.setText(hms);
                    gpsImage.setImageResource(R.drawable.circle_red);
                }
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    public boolean isGPSEnabled(){
        long currentTime = Calendar.getInstance().getTimeInMillis();
        return locationService.getUpdatedAt() > currentTime - 5000;
    }
}
