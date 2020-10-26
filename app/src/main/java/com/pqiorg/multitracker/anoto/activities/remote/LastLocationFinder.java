package com.pqiorg.multitracker.anoto.activities.remote;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import com.google.firebase.analytics.FirebaseAnalytics.Param;

public class LastLocationFinder implements ILastLocationFinder {

    /* renamed from: a */
    protected static String f3046a = "LastLocationFinder";

    /* renamed from: b */
    protected static String f3047b = "com.radioactiveyak.places.SINGLE_LOCATION_UPDATE_ACTION";

    /* renamed from: c */
    protected PendingIntent f3048c;

    /* renamed from: d */
    protected LocationListener f3049d;

    /* renamed from: e */
    protected LocationManager f3050e;

    /* renamed from: f */
    protected Context f3051f;

    /* renamed from: g */
    protected Criteria f3052g;

    /* renamed from: h */
    protected BroadcastReceiver f3053h = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            context.unregisterReceiver(LastLocationFinder.this.f3053h);
            Location location = (Location) intent.getExtras().get(Param.LOCATION);
            if (!(LastLocationFinder.this.f3049d == null || location == null)) {
                LastLocationFinder.this.f3049d.onLocationChanged(location);
            }
            LastLocationFinder.this.f3050e.removeUpdates(LastLocationFinder.this.f3048c);
        }
    };

    public LastLocationFinder(Context context) {
        this.f3051f = context;
        this.f3050e = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.f3052g = new Criteria();
        this.f3052g.setAccuracy(2);
        this.f3048c = PendingIntent.getBroadcast(context, 0, new Intent(f3047b), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void cancel() {
        try {
            this.f3051f.unregisterReceiver(this.f3053h);
        } catch (Exception unused) {
        }
        this.f3050e.removeUpdates(this.f3048c);
    }

    public Location getLastBestLocation(int i, long j) {
        long j2 = Long.MIN_VALUE;
        float f = Float.MAX_VALUE;
        Location location = null;
        try {
            for (String lastKnownLocation : this.f3050e.getAllProviders()) {
                Location lastKnownLocation2 = this.f3050e.getLastKnownLocation(lastKnownLocation);
                if (lastKnownLocation2 != null) {
                    float accuracy = lastKnownLocation2.getAccuracy();
                    long time = lastKnownLocation2.getTime();
                    if (time > j && accuracy < f) {
                        location = lastKnownLocation2;
                        f = accuracy;
                    } else if (time < j && f == Float.MAX_VALUE && time > j2) {
                        location = lastKnownLocation2;
                    }
                    j2 = time;
                }
            }
        }catch (SecurityException e){
            e.getCause();
        }

        if (this.f3049d == null || (j2 >= j && f <= ((float) i))) {
            return location;
        }
        this.f3051f.registerReceiver(this.f3053h, new IntentFilter(f3047b));
        try {
            this.f3050e.requestSingleUpdate(this.f3052g, this.f3048c);
        }catch (SecurityException e){
            e.getCause();
        }

        return null;
    }

    public void setChangedLocationListener(LocationListener locationListener) {
        this.f3049d = locationListener;
    }
}
