package pl.wat.nutpromobile.features.location;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import javax.inject.Inject;

import pl.wat.nutpromobile.activity.main.MainActivity;
import pl.wat.nutpromobile.features.service.ServiceManager;
import pl.wat.nutpromobile.features.training.TrainingService;


public class UserLocation implements LifecycleObserver, ServiceManager {
    private final static String TAG = "Custom: " + UserLocation.class.getSimpleName();

    private UserLocationService userLocationService;
    private MainActivity activity;
    private boolean shouldUnbindLocationService;
    private Lifecycle lifecycle;

    @Inject
    public UserLocation(MainActivity activity, Lifecycle lifecycle) {
        this.activity = activity;
        this.lifecycle = lifecycle;
        initUserLocation();
    }

    private void initUserLocation() {

    }

    public void addUserLocationListener(UserLocationListener userLocationListener) {
        if(userLocationService!=null)
            userLocationService.addUserLocationListener(userLocationListener);
    }

    public void removeUserLocationListener() {
        if(userLocationService!=null)
            userLocationService.removeUserLocationListener();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @Override
    public void bindService() {
        Intent intent = new Intent(activity, UserLocationService.class);
        activity.bindService(intent, userLocationServiceConnection, Context.BIND_AUTO_CREATE);
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


    public void stopService(){
        activity.stopService(new Intent(activity, UserLocationService.class));
    }

    private ServiceConnection userLocationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            userLocationService = ((UserLocationService.LocalBinder) iBinder).getService();
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
