package com.discoveries.jeremy.sunrisesunsetapp;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

/**
 * Created by User on 12.07.2018.
 */

public class GPS extends Service {

    private LocationManager locationManager;
    private LocationListener listener;
    private boolean runFirstTime;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        runFirstTime = true;
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                    if (runFirstTime) {
                        Intent intent = new Intent("check_location");
                        intent.putExtra("firstLongitude", String.valueOf(location.getLongitude()));
                        intent.putExtra("firstLatitude", String.valueOf(location.getLatitude()));
                        sendBroadcast(intent);
                        runFirstTime = false;
                    }
                    else if (!runFirstTime) {
                        Intent intent = new Intent("check_location");
                        intent.putExtra("nextLongitude", String.valueOf(location.getLongitude()));
                        intent.putExtra("nextLatitude", String.valueOf(location.getLatitude()));
                        sendBroadcast(intent);
                    }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection ConstantConditions
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(locationManager != null) locationManager.removeUpdates(listener);

    }

}
