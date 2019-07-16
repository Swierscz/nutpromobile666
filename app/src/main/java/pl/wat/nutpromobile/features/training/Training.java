package pl.wat.nutpromobile.features.training;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.Timer;
import java.util.TimerTask;

import pl.wat.nutpromobile.activity.main.MainActivity;
import pl.wat.nutpromobile.features.ble.BluetoothConnection;
import pl.wat.nutpromobile.features.ble.BluetoothLeService;
import pl.wat.nutpromobile.features.location.UserLocation;
import pl.wat.nutpromobile.features.location.UserLocationService;
import pl.wat.nutpromobile.features.service.ServiceManager;
import pl.wat.nutpromobile.features.service.ServiceUtil;

public class Training implements LifecycleObserver, ServiceManager {
    private final static String TAG = "Custom: " + Training.class.getSimpleName();

    private Activity activity;

    private BluetoothConnection bluetoothConnection;

    private UserLocation userLocation;

    private TrainingService trainingService;

    private Lifecycle lifecycle;

    private boolean shouldUnbindTrainingService;

    public Training(Activity activity, BluetoothConnection bluetoothConnection, UserLocation userLocation) {
        this.activity = activity;
        this.bluetoothConnection = bluetoothConnection;
        this.userLocation = userLocation;
    }

    public Training(Activity activity, Lifecycle lifecycle, BluetoothConnection bluetoothConnection, UserLocation userLocation) {
        this.activity = activity;
        this.bluetoothConnection = bluetoothConnection;
        this.userLocation = userLocation;
        this.lifecycle = lifecycle;
    }

    public void startTraining() {
        initTraining();
    }

    public void stopTraining() {
        ServiceUtil.stopForegroundService(activity, TrainingService.class);
        ServiceUtil.stopForegroundService(activity, BluetoothLeService.class);
        ServiceUtil.stopForegroundService(activity, UserLocationService.class);
        unbindService();
    }



    private void initTraining() {
        ServiceUtil.startForegroundService(activity, TrainingService.class);
        ServiceUtil.startForegroundService(activity, BluetoothLeService.class);
        ServiceUtil.startForegroundService(activity, UserLocationService.class);
    }

    public void addTrainingListener(TrainingListener trainingListener) {
        trainingService.addTrainingListener(trainingListener);
    }

    private ServiceConnection trainingServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            trainingService = ((TrainingService.LocalBinder) iBinder).getService();
            shouldUnbindTrainingService = true;
            addTrainingListener((MainActivity) activity);
            trainingService.handleTraining(bluetoothConnection, userLocation);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            trainingService = null;
        }
    };



    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void release() {
        unbindService();
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void resume() {
        bindService();
    }

    @Override
    public void bindService() {
            Intent intent = new Intent(activity, TrainingService.class);
            activity.bindService(intent, trainingServiceConnection, 0);
            shouldUnbindTrainingService = true;
    }

    @Override
    public void unbindService() {
        if (shouldUnbindTrainingService) {
            activity.unbindService(trainingServiceConnection);
            shouldUnbindTrainingService = false;
        }
    }
}
