package com.example.socialcompass;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.DataEntryPage;


@RunWith(RobolectricTestRunner.class)
public class US10Tests {
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    @Test
    public void testBoundaryInputs() {
        int tooLow = -1;
        int tooHigh = 360;
        int justRight1 = 359;
        int justRight2 = 0;

        boolean b1 = DataEntryPage.checkIfValidInput(tooLow);
        assertEquals(false, b1);

        boolean b2 = DataEntryPage.checkIfValidInput(tooHigh);
        assertEquals(false, b2);

        boolean b3 = DataEntryPage.checkIfValidInput(justRight1);
        assertEquals(true, b3);

        boolean b4 = DataEntryPage.checkIfValidInput(justRight2);
        assertEquals(true, b4);

    }
}
