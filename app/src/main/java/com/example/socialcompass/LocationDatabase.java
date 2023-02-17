package com.example.socialcompass;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Location.class}, version = 1)
public abstract class LocationDatabase extends RoomDatabase {

    private static LocationDatabase singleton = null;

    public abstract LocationDao locationDao();

    public synchronized  static LocationDatabase getSingleton(Context context) {
        if (singleton == null) {
            singleton = LocationDatabase.makeDatabase(context);
        }
        return singleton;
    }

    private static LocationDatabase makeDatabase(Context context) {
        return Room.databaseBuilder(context, LocationDatabase.class, "todo_app.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            List<Location> todos = Location
                                    .loadJSON(context, "demo_todos.json");
                            getSingleton(context).locationDao().insertAll(todos);
                        });
                    }
                })
                .build();
    }

    @VisibleForTesting
    public static void injectTestDatabase(LocationDatabase testDatabase) {
        if (singleton != null) {
            singleton.close();
        }
        singleton = testDatabase;
    }
}
