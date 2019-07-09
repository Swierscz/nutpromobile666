package pl.wat.nutpromobile.features.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import pl.wat.nutpromobile.util.NotificationCreator;

public class UserLocationService extends Service implements LocationListener {

    public final static String TAG = "Custom: " + UserLocationService.class.getSimpleName();

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // zmiana co 10 m
    private static final long MIN_TIME_BW_UPDATES = 1000 * 4;    // zmiana co minute

    private LocationManager locationManager;
    private boolean locationManagerIsEnabled = false;
    private boolean networkConnected = false;
    private Location location;
    private UserLocationListener userLocationListener;
    private IBinder mBinder;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mBinder = new LocalBinder();
        setupLocation(getBaseContext());
        startForeground(NotificationCreator.getNotificationId(), NotificationCreator.getNotification(getApplicationContext()));
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public UserLocationService getService() {
            return UserLocationService.this;
        }
    }

    public Location setupLocation(Context context) {
        System.out.println("SETUP");
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            locationManagerIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            networkConnected = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            Log.i(TAG, "GPS Enabled");
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else if (!locationManagerIsEnabled && networkConnected) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        } catch (SecurityException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (userLocationListener != null)
            userLocationListener.onUserLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void addUserLocationListener(UserLocationListener userLocationListener) {
        this.userLocationListener = userLocationListener;
    }

    public void removeUserLocationListener() {
        this.userLocationListener = null;
    }
}
