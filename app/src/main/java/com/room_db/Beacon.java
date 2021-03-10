package com.room_db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Beacon {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "dateTime")
    String dateTime;

    @ColumnInfo(name = "timestamp")
    private String timestamp;

    @ColumnInfo(name = "Name")
    private String Name;

    @ColumnInfo(name = "RSSI")
    private String RSSI;

    @ColumnInfo(name = "UUID")
    private String UUID;

    @ColumnInfo(name = "Major")
    private String Major;

    @ColumnInfo(name = "Minor")
    private String Minor;

    @ColumnInfo(name = "Battery")
    private String Battery;



    @ColumnInfo(name = "timestampBeacon")
    private String timestampBeacon;


   /* public Beacon(String dateTime, String timestamp, String Name, String RSSI, String UUID, String Major, String Minor, String Battery) {

        this.dateTime = dateTime;
        this.timestamp = timestamp;
        this.Name = Name;
        this.RSSI = RSSI;
        this.UUID = UUID;
        this.  Major = Major;
        this. Minor = Minor;
        this. Battery = Battery;

    }*/

    public Beacon( String dateTime, String timestamp, String Name, String RSSI, String UUID, String Major, String Minor, String Battery, String timestampBeacon) {

        this.dateTime = dateTime;
        this.timestamp = timestamp;
        this.Name = Name;
        this.RSSI = RSSI;
        this.UUID = UUID;
        this.Major = Major;
        this.Minor = Minor;
        this.Battery = Battery;
        this.timestampBeacon = timestampBeacon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRSSI() {
        return RSSI;
    }

    public void setRSSI(String RSSI) {
        this.RSSI = RSSI;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getMajor() {
        return Major;
    }

    public void setMajor(String major) {
        Major = major;
    }

    public String getMinor() {
        return Minor;
    }

    public void setMinor(String minor) {
        Minor = minor;
    }

    public String getBattery() {
        return Battery;
    }

    public void setBattery(String battery) {
        Battery = battery;
    }

    public String getTimestampBeacon() {
        return timestampBeacon;
    }

    public void setTimestampBeacon(String timestampBeacon) {
        this.timestampBeacon = timestampBeacon;
    }
}
