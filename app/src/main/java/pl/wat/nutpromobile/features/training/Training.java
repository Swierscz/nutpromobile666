package pl.wat.nutpromobile.features.training;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.Date;

import javax.inject.Inject;

import pl.wat.nutpromobile.NutproMobileApp;
import pl.wat.nutpromobile.activity.main.MainActivity;
import pl.wat.nutpromobile.db.repository.TrainingSummaryRepository;
import pl.wat.nutpromobile.db.row.TrainingSummaryRow;
import pl.wat.nutpromobile.features.ble.BluetoothConnection;
import pl.wat.nutpromobile.features.ble.BluetoothLeService;
import pl.wat.nutpromobile.features.location.UserLocation;
import pl.wat.nutpromobile.features.location.UserLocationService;
import pl.wat.nutpromobile.features.service.ServiceManager;
import pl.wat.nutpromobile.features.service.ServiceUtil;
import pl.wat.nutpromobile.model.TrainingSummary;
import pl.wat.nutpromobile.model.TrainingType;

public class Training implements LifecycleObserver, ServiceManager {
    private final static String TAG = "Custom: " + Training.class.getSimpleName();

    private Activity activity;

    private BluetoothConnection bluetoothConnection;

    private UserLocation userLocation;

    private TrainingService trainingService;

    private Lifecycle lifecycle;

    private boolean shouldUnbindTrainingService;

    private TrainingSummaryRepository trainingSummaryRepository;

    private TrainingSummary trainingSummary;

    private TrainingListener trainingListener;

    private TrainingType trainingType;

    @Inject
    public Training(MainActivity activity, BluetoothConnection bluetoothConnection, UserLocation userLocation) {
        this.activity = activity;
        this.bluetoothConnection = bluetoothConnection;
        this.userLocation = userLocation;
        this.lifecycle = activity.getLifecycle();
    }

    public void startTraining(TrainingType trainingType) {
        this.trainingType = trainingType;
        initTrainingService();
        // initTrainingMonitoring();
    }

    public void stopTraining() {
        saveTraining();
        activity.stopService(new Intent(activity, TrainingService.class));
        ServiceUtil.stopForegroundService(activity, TrainingService.class);
        ServiceUtil.stopForegroundService(activity, BluetoothLeService.class);
        ServiceUtil.stopForegroundService(activity, UserLocationService.class);
        unbindService();
    }


    public void setTrainingListener(TrainingListener trainingListener) {
        this.trainingListener = trainingListener;
    }

    private void initTrainingService() {
        ServiceUtil.startForegroundService(activity, TrainingService.class);
        ServiceUtil.startForegroundService(activity, BluetoothLeService.class);
        ServiceUtil.startForegroundService(activity, UserLocationService.class);
    }

    private void saveTraining() {/*
        if (trainingSummary != null) {
            trainingSummary.setStopTrainingTime(new Date());
            TrainingSummaryRow trainingSummaryRow = new TrainingSummaryRow();
            trainingSummaryRow.setStartTrainingTime(trainingSummary.getStartTrainingTime().toString());
            trainingSummaryRow.setStopTrainingTime(trainingSummary.getStopTrainingTime().toString());
            trainingSummaryRepository.insertTrainingSummary(trainingSummaryRow);
        }*/

        if(trainingService!=null){
            trainingSummary = trainingService.getTrainingSummaryData();
            if(trainingSummary != null){
                TrainingSummaryRow trainingSummaryRow = new TrainingSummaryRow();
                trainingSummaryRow.setStartTrainingTime(trainingSummary.getStartTrainingTime().toString());
                trainingSummaryRow.setStopTrainingTime(trainingSummary.getStopTrainingTime().toString());
                trainingSummaryRow.setDistance(trainingSummary.getDistance());
                trainingSummaryRow.setAverageSpeed(trainingSummary.getAverageSpeed());
                trainingSummaryRow.setTrainingType(trainingType.toString());
                trainingSummaryRepository.insertTrainingSummary(trainingSummaryRow);
            }

        }
    }

    private void initTrainingMonitoring() {
        trainingSummary = new TrainingSummary();
        trainingSummary.setStartTrainingTime(new Date());
    }

    private void addTrainingListener() {
        if (trainingService != null) {
            trainingService.addTrainingListener(trainingListener);
        }
    }

    public void removeTrainingListener() {
        if (trainingService != null) {
            trainingService.removeTrainingListener();
        }
    }

    private ServiceConnection trainingServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            trainingService = ((TrainingService.LocalBinder) iBinder).getService();
            shouldUnbindTrainingService = true;
            addTrainingListener();
            trainingService.handleTraining(bluetoothConnection, userLocation, new Date(), trainingType);
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
