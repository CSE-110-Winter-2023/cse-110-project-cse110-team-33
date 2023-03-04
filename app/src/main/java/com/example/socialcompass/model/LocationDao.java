package com.example.socialcompass.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LocationDao {

    @Insert
    long insert(Location location);

    @Query("SELECT * FROM `locations` WHERE `id`=:id")
    Location get(long id);

    @Query("SELECT * FROM `locations` ORDER BY public_code")
    List<Location> getAll();

    @Update
    int update(Location location);

    @Delete
    int delete(Location location);

    @Insert
    List<Long> insertAll(List<Location> locations);

    @Query("SELECT * FROM `locations` ORDER BY public_code")
    LiveData<List<Location>> getAllLive();

    @Query("SELECT public_code + 1 FROM `locations` ORDER BY public_code DESC LIMIT 1")
    int getOrderForAppend();
}
