package com.pqiorg.multitracker.anoto.activities.remote;

import android.location.Location;
import android.location.LocationListener;

public interface ILastLocationFinder {
    void cancel();

    Location getLastBestLocation(int i, long j);

    void setChangedLocationListener(LocationListener locationListener);
}
