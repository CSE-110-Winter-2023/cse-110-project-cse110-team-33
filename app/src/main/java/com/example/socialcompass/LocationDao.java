package com.example.socialcompass;

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

    @Query("SELECT * FROM `todo_list_items` WHERE `id`=:id")
    Location get(long id);

    @Query("SELECT * FROM `todo_list_items` ORDER BY `order`")
    List<Location> getAll();

    @Update
    int update(Location location);

    @Delete
    int delete(Location location);

    @Insert
    List<Long> insertAll(List<Location> locations);

    @Query("SELECT * FROM `todo_list_items` ORDER BY `order`")
    LiveData<List<Location>> getAllLive();

    @Query("SELECT `order` + 1 FROM `todo_list_items` ORDER BY `order` DESC LIMIT 1")
    int getOrderForAppend();
}
