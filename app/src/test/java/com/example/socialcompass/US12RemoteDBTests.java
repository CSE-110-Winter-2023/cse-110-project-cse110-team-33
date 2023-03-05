package com.example.socialcompass;

import com.example.socialcompass.model.Location;
import com.example.socialcompass.model.LocationAPI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import static org.junit.Assert.*;

import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class US12RemoteDBTests {

    String public_code = "a40bc854-249f-4872-a114-a468fe413dab";
    String private_code = "a5f0aafe-4eef-45d4-83dc-ba8f7a3fdb1c";
    Location location;

    @Before
    public void setUp() {
        location = new Location(public_code,
                32.32,
                32.32,
                "33-test-location");

    }

    @Test
    public void putLocation() throws ExecutionException, InterruptedException {
        LocationAPI api = LocationAPI.provide();

        System.out.println(public_code);

        Future<String> result = api.putAsync(location, private_code);
        String response = result.get();
        System.out.println(response);

        Location returnedLocation = Location.fromJSON(response);

        assertEquals(location.label, returnedLocation.label);
        assertEquals(location.public_code, returnedLocation.public_code);
    }

    @Test
    public void getLocation() throws ExecutionException, InterruptedException {
        LocationAPI api = LocationAPI.provide();

        Future<Location> result = api.getAsync(public_code);
        System.out.println(result.get().toString());

        assertEquals(location.label, result.get().label);

    }

    @Test
    public void patchLocation() throws ExecutionException, InterruptedException {
        LocationAPI api = LocationAPI.provide();

        location.latitude = 64;
        location.longitude = 64;

        Future<String> result = api.patchAsync(public_code, private_code, location.latitude, location.longitude);
        Location locationFromApi = Location.fromJSON(result.get());

        assertEquals(location.latitude, locationFromApi.latitude, 0.01);


    }

    @Test
    public void deleteLocation() throws ExecutionException, InterruptedException {
        LocationAPI api = LocationAPI.provide();

        Future<String> result = api.deleteAsync(location, private_code);
        assertEquals("{\"message\":\"Location deleted.\"}", result.get());
    }

    @Test
    public void getNonexistentLocation() throws ExecutionException, InterruptedException {
        LocationAPI api = LocationAPI.provide();

        Future<Location> result = api.getAsync(public_code);
        System.out.println(result.get().toString());

        assertNull(result.get().public_code);
    }
}
