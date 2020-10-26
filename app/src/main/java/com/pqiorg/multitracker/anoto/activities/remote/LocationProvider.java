package com.pqiorg.multitracker.anoto.activities.remote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.rabbitmq.client.ConnectionFactory;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;

public class LocationProvider {
    private static final String TAG = "LocationProvider";
    /* access modifiers changed from: private */
    public static long _geoCachePolicy = 300000;
    public final int DEFAULT_RADIUS = 150;
    public final int MAX_DISTANCE = 75;
    public final long MAX_TIME = 300000;
    private Context _context;
    /* access modifiers changed from: private */
    public ILastLocationFinder _lastLocationFinder;
    private Timer _locationUpdateTimer;
    private boolean _started;

    /* renamed from: a */
    protected LocationListener f3055a = new LocationListener() {
        public void onLocationChanged(Location location) {
            Log.i(LocationProvider.TAG, "LocationListener > onLocationChanged()");
            if (location != null) {
                String str = LocationProvider.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("LocationListener > onLocationChanged(): ");
                sb.append(location.getAccuracy());
                sb.append(ConnectionFactory.DEFAULT_VHOST);
                sb.append(location.getLatitude());
                sb.append(ConnectionFactory.DEFAULT_VHOST);
                sb.append(location.getLongitude());
                Log.i(str, sb.toString());
                LocationProvider.this.mo10062a(location);
            }
        }

        public void onProviderDisabled(String str) {
            Log.i(LocationProvider.TAG, "LocationListener > onProviderDisabled()");
        }

        public void onProviderEnabled(String str) {
            Log.i(LocationProvider.TAG, "LocationListener > onProviderEnabled()");
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
            Log.i(LocationProvider.TAG, "LocationListener > onStatusChanged()");
        }
    };
    private Location handsetLocation;

    private class LocationTimerTask extends TimerTask {
        private LocationTimerTask() {
        }

        public void run() {
            Log.i(LocationProvider.TAG, "LocationTimerTask > run()");
            try {
                Location lastBestLocation = LocationProvider.this._lastLocationFinder.getLastBestLocation(75, System.currentTimeMillis() - LocationProvider._geoCachePolicy);
                if (lastBestLocation != null) {
                    String str = LocationProvider.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("LocationTimerTask(): ");
                    sb.append(lastBestLocation.getAccuracy());
                    sb.append("   /   ");
                    sb.append(lastBestLocation.getLatitude());
                    sb.append("   /   ");
                    sb.append(lastBestLocation.getLongitude());
                    sb.append(")");
                    Log.i(str, sb.toString());
                    LocationProvider.this.mo10062a(lastBestLocation);
                    return;
                }
                Log.i(LocationProvider.TAG, "LocationTimerTask(): returned NULL");
                LocationProvider.this.getMyLocation();
            } catch (Exception e) {
                String str2 = LocationProvider.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("LocationTimerTask(): Exception ");
                sb2.append(e.toString());
                Log.i(str2, sb2.toString());
            }
        }
    }

    public LocationProvider(Context context) {
        this._context = context;
    }

    /* access modifiers changed from: private */
    @SuppressLint({"MissingPermission"})
    public Location getMyLocation() {
        Location location = null;
        try {
            LocationManager locationManager = (LocationManager) this._context.getSystemService(Context.LOCATION_SERVICE);
            boolean isProviderEnabled = locationManager.isProviderEnabled("gps");
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("isGPSEnabled : ");
            sb.append(isProviderEnabled);
            Log.d(str, sb.toString());
            boolean isProviderEnabled2 = locationManager.isProviderEnabled("network");
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("isNetworkEnabled : ");
            sb2.append(isProviderEnabled2);
            Log.d(str2, sb2.toString());
            if (isProviderEnabled || isProviderEnabled2) {
                if (isProviderEnabled2) {
                    locationManager.requestLocationUpdates("network", 60000, 10.0f, this.f3055a);
                    Log.d(TAG, "Network Enabled");
                    if (locationManager != null) {
                        Location lastKnownLocation = locationManager.getLastKnownLocation("network");
                        if (lastKnownLocation != null) {
                            String str3 = TAG;
                            try {
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("Network Enabled : ");
                                sb3.append(lastKnownLocation.getLatitude());
                                sb3.append(" / ");
                                sb3.append(lastKnownLocation.getLongitude());
                                Log.d(str3, sb3.toString());
                            } catch (Exception e) {
                                e = e;
                                location = lastKnownLocation;
                            }
                        }
                        location = lastKnownLocation;
                    }
                }
                if (isProviderEnabled && location == null) {
                    locationManager.requestLocationUpdates("gps", 60000, 10.0f, this.f3055a);
                    Log.d(TAG, "GPS Enabled");
                    if (locationManager != null) {
                        Location lastKnownLocation2 = locationManager.getLastKnownLocation("gps");
                        if (lastKnownLocation2 != null) {
                            String str4 = TAG;
                            try {
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("GPS Enabled : ");
                                sb4.append(lastKnownLocation2.getLatitude());
                                sb4.append(" / ");
                                sb4.append(lastKnownLocation2.getLongitude());
                                Log.d(str4, sb4.toString());
                            } catch (Exception e2) {
                                Location location2 = lastKnownLocation2;
                                e2 = e2;
                                location = location2;
                            }
                        }
                        return lastKnownLocation2;
                    }
                }
                return location;
            }
            Log.d(TAG, "DisEnabled : location null");
            return null;
        } catch (Exception e3) {
            e3 = e3;
            e3.printStackTrace();
            return location;
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public synchronized Location mo10061a() {
        Log.i(TAG, "getHandsetLocation()");
        if (!this._started) {
            start();
            return null;
        }
        return this.handsetLocation;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public synchronized void mo10062a(Location location) {
        Log.i(TAG, "setHandsetLocation()");
        this.handsetLocation = location;
    }

    public synchronized JSONObject getHandsetLocationJSON() {
        Log.i(TAG, "getHandsetLocationJSON()");
        Location a = mo10061a();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getHandsetLocationJSON(): ");
        sb.append(a);
        Log.i(str, sb.toString());
        if (a != null) {
            try {
                return new JSONObject().put("Latitude", a.getLatitude()).put("Longitude", a.getLongitude());
            } catch (JSONException unused) {
            }
        }
        return null;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:5|6|7|8) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0011 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void start() {
        /*
            r7 = this;
            monitor-enter(r7)
            java.lang.String r0 = "LocationProvider"
            java.lang.String r1 = "start()"
            android.util.Log.i(r0, r1)     // Catch:{ all -> 0x003d }
            boolean r0 = r7._started     // Catch:{ all -> 0x003d }
            if (r0 != 0) goto L_0x003b
            java.util.Timer r0 = r7._locationUpdateTimer     // Catch:{ Exception -> 0x0011 }
            r0.cancel()     // Catch:{ Exception -> 0x0011 }
        L_0x0011:
            com.anoto.adna.remote.LastLocationFinder r0 = new com.anoto.adna.remote.LastLocationFinder     // Catch:{ all -> 0x003d }
            android.content.Context r1 = r7._context     // Catch:{ all -> 0x003d }
            r0.<init>(r1)     // Catch:{ all -> 0x003d }
            r7._lastLocationFinder = r0     // Catch:{ all -> 0x003d }
            com.anoto.adna.remote.ILastLocationFinder r0 = r7._lastLocationFinder     // Catch:{ all -> 0x003d }
            android.location.LocationListener r1 = r7.f3055a     // Catch:{ all -> 0x003d }
            r0.setChangedLocationListener(r1)     // Catch:{ all -> 0x003d }
            java.util.Timer r0 = new java.util.Timer     // Catch:{ all -> 0x003d }
            r0.<init>()     // Catch:{ all -> 0x003d }
            r7._locationUpdateTimer = r0     // Catch:{ all -> 0x003d }
            java.util.Timer r1 = r7._locationUpdateTimer     // Catch:{ all -> 0x003d }
            com.anoto.adna.remote.LocationProvider$LocationTimerTask r2 = new com.anoto.adna.remote.LocationProvider$LocationTimerTask     // Catch:{ all -> 0x003d }
            r0 = 0
            r2.<init>()     // Catch:{ all -> 0x003d }
            r3 = 0
            r5 = 180000(0x2bf20, double:8.8932E-319)
            r1.schedule(r2, r3, r5)     // Catch:{ all -> 0x003d }
            r0 = 1
            r7._started = r0     // Catch:{ all -> 0x003d }
        L_0x003b:
            monitor-exit(r7)
            return
        L_0x003d:
            r0 = move-exception
            monitor-exit(r7)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.remote.LocationProvider.start():void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:0|1|2|3|4|5|7) */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x000c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void stop() {
        /*
            r2 = this;
            java.lang.String r0 = "LocationProvider"
            java.lang.String r1 = "stop()"
            android.util.Log.i(r0, r1)
            java.util.Timer r0 = r2._locationUpdateTimer     // Catch:{ Exception -> 0x000c }
            r0.cancel()     // Catch:{ Exception -> 0x000c }
        L_0x000c:
            com.anoto.adna.remote.ILastLocationFinder r0 = r2._lastLocationFinder     // Catch:{ Exception -> 0x0011 }
            r0.cancel()     // Catch:{ Exception -> 0x0011 }
        L_0x0011:
            r0 = 0
            r2._locationUpdateTimer = r0
            r2._lastLocationFinder = r0
            r0 = 0
            r2._started = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.remote.LocationProvider.stop():void");
    }
}
