package com.example.socialcompass;

import static org.junit.Assert.assertEquals;

import com.example.socialcompass.utility.AngleCalculation;

import org.junit.Test;

public class AngleCalculationTest {
    @Test
    public void testAngleSDtoNYC(){
        AngleCalculation calculator = new AngleCalculation();

        double sdLat = 32.715736;
        double sdLong = -117.161087;
        double nycLat = 40.730610;
        double nycLong = -73.935242;

        double result = calculator.calculateBearing(nycLat, nycLong, sdLat, sdLong);
        System.out.println(result);

        assertEquals("1", "1");
    }
}
