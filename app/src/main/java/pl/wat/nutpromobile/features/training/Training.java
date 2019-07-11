package pl.wat.nutpromobile.features.training;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.Date;

import pl.wat.nutpromobile.NutproMobileApp;
import pl.wat.nutpromobile.activity.main.MainActivity;
import pl.wat.nutpromobile.db.repository.TrainingSummaryRepository;
import pl.wat.nutpromobile.db.row.TrainingSummaryRow;
import pl.wat.nutpromobile.features.ble.Connection;
import pl.wat.nutpromobile.features.location.UserLocation;
import pl.wat.nutpromobile.model.TrainingSummary;

public class Training {
    private final static String TAG = "Custom: " + Training.class.getSimpleName();

    private Activity activity;

    private Connection connection;

    private UserLocation userLocation;

    private TrainingService trainingService;

    private TrainingSummaryRepository trainingSummaryRepository;

    private TrainingSummary trainingSummary;

    public Training(Activity activity, Connection connection, UserLocation userLocation) {
        this.activity = activity;
        this.connection = connection;
        this.userLocation = userLocation;
        this.trainingSummaryRepository = ((NutproMobileApp)activity.getApplication()).repositoryProvider
                .getTrainingSummaryRepositoryInstance((NutproMobileApp)activity.getApplication());
    }

    public void startTraining() {
        initTrainingService();
        initTrainingMonitoring();
    }

    public void stopTraining() {
        saveTraining();
        activity.stopService(new Intent(activity, TrainingService.class));
    }

    private void initTrainingService() {
        activity.startService(new Intent(activity, TrainingService.class));
        if (trainingService == null) {
            activity.bindService(new Intent(activity, TrainingService.class), trainingServiceConnection, 0);
        }
    }

    private void saveTraining() {
        trainingSummary.setStopTrainingTime(new Date());
        TrainingSummaryRow trainingSummaryRow = new TrainingSummaryRow();
        trainingSummaryRow.setStartTrainingTime(trainingSummary.getStartTrainingTime().toString());
        trainingSummaryRow.setStopTrainingTime(trainingSummary.getStopTrainingTime().toString());
        trainingSummaryRepository.insertTrainingSummary(trainingSummaryRow);
    }

    private void initTrainingMonitoring() {
        trainingSummary = new TrainingSummary();
        trainingSummary.setStartTrainingTime(new Date());
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
