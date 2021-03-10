package com.room_db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;



import java.util.List;

@Dao
public interface BeaconDao {

    @Query("SELECT * FROM Beacon")
    List<Beacon> getAll();

    @Query("SELECT * FROM Beacon WHERE timestampBeacon = :beaconTimestamp")
    List<Beacon> findByTimestamp(String beaconTimestamp);

    @Query("SELECT * FROM Beacon WHERE timestampBeacon = :beaconTimestamp")
    List<Beacon> findByTimestampNew(String beaconTimestamp);

    @Insert
    void insert(Beacon beacon);

    @Delete
    void delete(Beacon beacon);

    @Update
    void update(Beacon beacon);




}
