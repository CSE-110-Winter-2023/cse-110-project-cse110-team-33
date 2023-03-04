package com.example.socialcompass.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.List;

@Dao
public interface LocationDao {

    @Upsert
    public abstract long upsert(Location loc);

    @Query("SELECT EXISTS(SELECT 1 FROM locations WHERE public_code = :public_code)")
    public abstract boolean exists(String public_code);

    @Query("SELECT * FROM `locations` WHERE `public_code`=:public_code")
    public abstract LiveData<Location> get(String public_code);

    @Query("SELECT * FROM `locations` ORDER BY public_code")
    public abstract LiveData<List<Location>> getAll();

    @Insert
    long insert(Location location);

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
    ;
}
