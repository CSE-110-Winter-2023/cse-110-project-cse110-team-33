package com.example.socialcompass;

import static org.junit.Assert.*;

import com.example.socialcompass.utility.DistanceCalculation;

import org.junit.Test;

public class US13Tests {

    @Test
    public void testCalcualteDistance() {
        DistanceCalculation calculator = new DistanceCalculation();
        double lat1;
        double long1;
        double lat2;
        double long2;

        assertEquals(1, calculator.CalculateDistance(lat1, long1, lat2, long2));
    }
}
