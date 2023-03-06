package com.example.socialcompass;

import static org.junit.Assert.*;

import com.example.socialcompass.utility.DistanceCalculation;

import org.junit.Test;

public class US13Tests {

    // accepted error range
    private final double DELTA = 1e-9;

    @Test
    public void testCalcualteDistance() {
        DistanceCalculation calculator = new DistanceCalculation();
        double lat1 = 0;
        double long1 = 0;
        double lat2 = 90;
        double long2 = 90;

        assertEquals(10007.543398, calculator.CalculateDistance(lat1, long1, lat2, long2), DELTA);
    }
}
