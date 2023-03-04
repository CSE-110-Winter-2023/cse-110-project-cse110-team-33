package com.example.socialcompass.utility;

import static java.lang.Math.*;
import androidx.appcompat.app.AppCompatActivity;

//calculates the bearing (in degrees) between two geographic coordinates
// using the Haversine formula.
public class AngleCalculation {


    private static final double RADIANS_TO_DEGREES = 180 / PI;
    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM
    public static double calculateBearing(double lat1, double long1, double lat2, double long2) {
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double deltaLongitude = Math.toRadians(long2 - long1);

        double y = Math.sin(deltaLongitude) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) -
                Math.sin(lat1) * Math.cos(lat2) * Math.cos(deltaLongitude);
        double bearing = Math.atan2(y, x);

        bearing = Math.toDegrees(bearing);
        if (bearing < 0) {
            bearing += 360;
        }

        return bearing;
    }
}
