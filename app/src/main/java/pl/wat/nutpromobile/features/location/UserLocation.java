package pl.wat.nutpromobile.features.location;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import pl.wat.nutpromobile.features.service.ServiceManager;
import pl.wat.nutpromobile.util.ServiceForegroundChecker;


public class UserLocation implements LifecycleObserver, ServiceManager {
    private final static String TAG = "Custom: " + UserLocation.class.getSimpleName();

    private UserLocationService userLocationService;
    private Activity activity;
    private boolean shouldUnbindLocationService;
    private Lifecycle lifecycle;

    public UserLocation(Activity activity) {
        this.activity = activity;
        initUserLocation();
    }

     public UserLocation(Activity activity, Lifecycle lifecycle){
        this.activity = activity;
        this.lifecycle = lifecycle;
     }

    private void initUserLocation() {
        startServiceAsForeground();
    }

    public void addUserLocationListener(UserLocationListener userLocationListener) {
        userLocationService.addUserLocationListener(userLocationListener);
    }

    public void removeUserLocationListener() {
        userLocationService.removeUserLocationListener();
    }

    void startServiceAsForeground(){
        if (!ServiceForegroundChecker.isServiceRunningInForeground(activity, UserLocationService.class)) {
            activity.startService(new Intent(activity, UserLocationService.class));
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @Override
    public void bindService(){
        Intent gattServiceIntent = new Intent(activity, UserLocationService.class);
        activity.bindService(gattServiceIntent, userLocationServiceConnection, Context.BIND_AUTO_CREATE);
        shouldUnbindLocationService = true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    @Override
    public void unbindService() {
        if (shouldUnbindLocationService) {
            activity.unbindService(userLocationServiceConnection);
            shouldUnbindLocationService = false;
        }
    }


    private ServiceConnection userLocationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            userLocationService =((UserLocationService.LocalBinder)iBinder).getService();
            shouldUnbindLocationService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            userLocationService = null;
        }
    };

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    @Override
    public void release() {
        unbindService();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @Override
    public void resume() {
        bindService();
    }
}
