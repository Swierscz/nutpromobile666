package pl.wat.nutpromobile.features.training;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import pl.wat.nutpromobile.activity.main.MainActivity;
import pl.wat.nutpromobile.features.ble.Connection;
import pl.wat.nutpromobile.features.location.UserLocation;

public class Training {
    private final static String TAG = "Custom: " + Training.class.getSimpleName();

    private Activity activity;

    private Connection connection;

    private UserLocation userLocation;

    private TrainingService trainingService;

    public Training(Activity activity, Connection connection, UserLocation userLocation) {
        this.activity = activity;
        this.connection = connection;
        this.userLocation = userLocation;
    }

    public void startTraining() {
        initTrainingService();
    }

    public void stopTraining() {
        activity.stopService(new Intent(activity, TrainingService.class));
    }

    private void initTrainingService() {
        activity.startService(new Intent(activity, TrainingService.class));
        if (trainingService == null) {
            activity.bindService(new Intent(activity, TrainingService.class), trainingServiceConnection, 0);
        }
    }

    public void addTrainingListener(TrainingListener trainingListener) {
        if (trainingService != null ) {
            trainingService.addTrainingListener(trainingListener);
        }
    }

    public void removeTrainingListener() {
        if (trainingService != null ) {
            trainingService.removeTrainingListener();
        }
    }

    private ServiceConnection trainingServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            TrainingService.LocalBinder binder = (TrainingService.LocalBinder) iBinder;
            trainingService = binder.getService();
            addTrainingListener((MainActivity)activity);
            trainingService.handleTraining(connection, userLocation);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            trainingService = null;
        }
    };
}
