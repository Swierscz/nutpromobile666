package pl.wat.nutpromobile.features.location;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;


public class UserLocation {
    private final static String TAG = "Custom: " + UserLocation.class.getSimpleName();

    private UserLocationService userLocationService;
    private Activity activity;

    public UserLocation(Activity activity) {
        this.activity = activity;
        initUserLocation();
    }

    private void initUserLocation() {
        activity.startService(new Intent(activity, UserLocationService.class));
        activity.bindService(new Intent(activity, UserLocationService.class), userLocationServiceConnection, 0);
    }

    public void addUserLocationListener(UserLocationListener userLocationListener) {
        userLocationService.addUserLocationListener(userLocationListener);
    }

    public void removeUserLocationListener() {
        userLocationService.removeUserLocationListener();
    }

    private ServiceConnection userLocationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            UserLocationService.LocalBinder binder = (UserLocationService.LocalBinder) iBinder;
            userLocationService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            userLocationService = null;
        }
    };
}
