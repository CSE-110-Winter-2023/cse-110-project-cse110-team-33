package com.example.socialcompass.utility;

public class DistanceCalculation {

    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

    /**
     * returns the distance (in kilometers) between two locations, given the longitude and latitude
     * of the two locations
     *
     * @param userLat Latitude of the user
     * @param userLong Longitude of the user
     * @param friendLat Latitude of the friend
     * @param friendLong Longitude of the friend
     * @return the distance, in kilometers, between the two given locations
     *
     * Source: https://www.geeksforgeeks.org/program-distance-two-points-earth/
     */
    public double CalculateDistance(double userLat, double userLong, double friendLat, double friendLong) {

        double userLatRad = Math.toRadians(userLat);
        double userLongRad = Math.toRadians(userLong);
        double friendLatRad = Math.toRadians(friendLat);
        double friendLongRad = Math.toRadians(friendLong);

        double diffLat = friendLatRad - userLatRad;
        double diffLong = friendLongRad - userLongRad;

        double haversine = Math.pow(Math.sin(diffLat / 2), 2)
                         + Math.cos(userLatRad) * Math.cos(friendLatRad)
                         * Math.pow(Math.sin(diffLong / 2), 2);

        double distance = 2 * Math.asin(Math.sqrt(haversine)) * EARTH_RADIUS;

        return distance;
        /*
        double sinArgument = Math.sin(userLat) * Math.sin(friendLat);
        double cosArgument = Math.cos(userLat) * Math.cos(friendLat) * Math.cos(friendLong - userLong);

        double distance = Math.acos(sinArgument + cosArgument) * EARTH_RADIUS;

        return distance;
         */
    }
}
