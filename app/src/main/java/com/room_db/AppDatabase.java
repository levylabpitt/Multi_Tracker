package com.room_db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.synapse.model.TaskData;

@Database(entities = {TaskData.class,Beacon.class}, version = 8, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
    public abstract BeaconDao beaconDao();
}
