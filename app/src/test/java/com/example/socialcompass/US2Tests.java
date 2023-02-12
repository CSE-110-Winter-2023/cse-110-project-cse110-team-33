package com.example.socialcompass;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class US2Tests {

    @Test
    public void testCalculateBearing_ForValidInputs() {
        double lat1 = 40.730610;
        double long1 = -73.935242;
        double lat2 = 48.856613;
        double long2 = 2.352222;
        double expectedBearing = 53.737273807858585;

        double actualBearing = AngleCalculation.calculateBearing(lat1, long1, lat2, long2);
        assertEquals(expectedBearing, actualBearing, 0.1);
    }

    @Test
    public void testCalculateBearing_ForBoundaryInputs() {
        double lat1 = 90;
        double long1 = 180;
        double lat2 = -90;
        double long2 = -180;
        double expectedBearing = 180;

        double actualBearing = AngleCalculation.calculateBearing(lat1, long1, lat2, long2);
        assertEquals(expectedBearing, actualBearing, 0.1);
    }
}
