package com.example.socialcompass.utility;

public class DistanceCalculation {

    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

    /**
     * returns the distance (in kilometers) between two locations, given the longitude and latitude
     * of the two locations
     *
     * @param userLat    Latitude of the user
     * @param userLong   Longitude of the user
     * @param friendLat  Latitude of the friend
     * @param friendLong Longitude of the friend
     * @return the distance, in miles, between the two given locations
     * <p>
     * Source: https://www.geeksforgeeks.org/program-distance-two-points-earth/
     */
    public static double CalculateDistance(double userLat, double userLong, double friendLat, double friendLong) {
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

        return distance * 0.621371;
    }

    public static double pixelCalculator(double level, double parentRadius, double actualDistance) {
        double levelPixel = parentRadius * (1.0 / level);

        int[] arr = {0, 1, 10, 500, 1000};
        double returnRadius = 0;
        double smallerRadius = 0;
        double biggerRadius = 0;
        int currentSmallerLevel = 0;
        //System.out.println(actualDistance);
        if (actualDistance >= 1000) {
            smallerRadius = 1000;
            currentSmallerLevel = 4;
        } else if (actualDistance >= 500) {
            smallerRadius = 500;
            biggerRadius = 1000;
            currentSmallerLevel = 3;
        } else if (actualDistance >= 10) {
            smallerRadius = 10;
            biggerRadius = 500;
            currentSmallerLevel = 2;
        } else if (actualDistance >= 1) {
            smallerRadius = 1;
            biggerRadius = 10;
            currentSmallerLevel = 1;
        } else {
            smallerRadius = 0;
            biggerRadius = 1;
            currentSmallerLevel = 0;
        }
        if (smallerRadius >= arr[(int) level]) {
            //System.out.println("in");

            returnRadius = parentRadius;
        } else {
            double temp = (actualDistance - smallerRadius) / (biggerRadius - smallerRadius);
            returnRadius = levelPixel * (currentSmallerLevel) + temp * levelPixel;
           // System.out.println(level);

        }
        return returnRadius;
    }
}
