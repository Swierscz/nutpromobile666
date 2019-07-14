package pl.wat.nutpromobile.features.training;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import pl.wat.nutpromobile.features.ble.Connection;
import pl.wat.nutpromobile.features.ble.ConnectionListener;
import pl.wat.nutpromobile.features.location.UserLocation;
import pl.wat.nutpromobile.features.location.UserLocationListener;
import pl.wat.nutpromobile.model.SensoricData;
import pl.wat.nutpromobile.model.TrainingData;
import pl.wat.nutpromobile.model.TrainingSummary;
import pl.wat.nutpromobile.model.TrainingType;
import pl.wat.nutpromobile.util.NotificationCreator;

public class TrainingService extends Service implements UserLocationListener, ConnectionListener {
    private final static String TAG = TrainingService.class.getSimpleName();

    private final int INTERVAL = 50;
    private final String TIME_DIVIDER = ":";

    private String staticFileName;

    private String storagePath;

    private BufferedWriter bufferedWriter;

    private IBinder mBinder;

    private Context context;

    private Connection connection;

    private UserLocation userLocation;

    private TrainingListener trainingListener;

    private Location location;

    private Handler m_handler = new Handler();

    private float distance;

    private float distanceToSpeedMeasurement;

    private Date timeToSpeedMeasurement;

    private Date startTime;

    private int speedCounter;

    private float amountOfSpeedMeasurement;

    private int speedMeasurements;

    private TrainingType trainingType;

    @Override
    public void onCreate() {
        super.onCreate();
        staticFileName = "sensorData";
        storagePath = Environment.getExternalStorageDirectory().getPath();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if ("stop".equals(intent.getAction())) {
            Log.i(TAG, "called to cancel service");
            prepareServiceToStop();
            stopSelf();
        } else {
            Log.i(TAG, TAG + " foreground started");
            mBinder = new LocalBinder();
            context = getBaseContext();
            timeToSpeedMeasurement = new Date();
            distanceToSpeedMeasurement = 0;
            speedCounter = 0;
            amountOfSpeedMeasurement = 0;
            distance = 0;
            speedMeasurements = 0;
            startForeground(NotificationCreator.getNotificationId(), NotificationCreator.getNotification(getApplicationContext()));
        }
        return START_STICKY;
    }

    public void handleTraining(Connection connection, UserLocation userLocation, Date startTime, TrainingType trainingType) {
        if (this.connection != null || this.userLocation != null)
            return;
        this.connection = connection;
        this.userLocation = userLocation;
        this.startTime = startTime;
        this.trainingType = trainingType;
        userLocation.addUserLocationListener(this);
        connection.addConnectionListener(this);
        connection.sendCommand("a");
        handlerTimerTask.run();
        File dir = new File(storagePath + "/NutproMobile");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            String fileName = staticFileName + "_" + new Date();
            File file = new File(dir, fileName);
            bufferedWriter = new BufferedWriter(new FileWriter(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, TAG + " unbind");
        prepareServiceToStop();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, TAG + " destroyed");
        prepareServiceToStop();
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public TrainingSummary getTrainingSummaryData() {
        TrainingSummary trainingSummary = new TrainingSummary();
        if (speedMeasurements != 0) {
            trainingSummary.setAverageSpeed(amountOfSpeedMeasurement/speedMeasurements);
        } else {
            trainingSummary.setAverageSpeed(0);
        }
        trainingSummary.setStartTrainingTime(startTime);
        trainingSummary.setStopTrainingTime(new Date());
        trainingSummary.setDistance(distance);
        trainingSummary.setTrainingType(trainingType.name());
        return trainingSummary;
    }

    @Override
    public void onUserLocationChanged(Location location) {
        calculateDistance(location);
        this.location = location;
        //trainingListener.onTrainingDataProcessed(new TrainingData(null, location, distance));
        trainingListener.onDistanceChange(String.valueOf(distance));
        speedCounter++;
        if (speedCounter > 1) {
            speedCounter = 0;
            float speed = calculateSpeed();
            amountOfSpeedMeasurement =+ speed;
            speedMeasurements++;
            trainingListener.onSpeedChange(String.valueOf(speed));
        }
    }

    @Override
    public void onDataReceived(SensoricData sensoricData) {
        if (trainingListener != null) {
            trainingListener.onTrainingDataProcessed(new TrainingData(sensoricData, null, distance));
        }
    }

    public class LocalBinder extends Binder {
        public TrainingService getService() {
            return TrainingService.this;
        }
    }

    private void saveDataToFile(String data) {
        try {
            bufferedWriter.append(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calculateDistance(Location location) {
        if (this.location != null) {
            distance = distance + this.location.distanceTo(location);
        }
    }

    private float calculateSpeed() {
        Date currentTime = new Date();
        float t = Math.abs((currentTime.getTime()/1000) - timeToSpeedMeasurement.getTime()/1000);
        System.out.println("time: " + t);
        float d = distance - distanceToSpeedMeasurement;
        distanceToSpeedMeasurement = distance;
        return d/t;
    }

    public void addTrainingListener(TrainingListener trainingListener) {
        this.trainingListener = trainingListener;
    }

    public void removeTrainingListener() {
        this.trainingListener = null;
    }

    private void prepareServiceToStop() {
        connection.sendCommand("q");
        m_handler.removeCallbacks(handlerTimerTask);
        userLocation.removeUserLocationListener();
        connection.removeConnectionListener();
    }

    final Runnable handlerTimerTask = new Runnable()
    {
        @Override
        public void run() {
            // POPRAWIC
            Date currentTime = new Date();
            Date date = new Date(Math.abs(currentTime.getTime() - startTime.getTime()) - 3600 * 1000);
            String time = android.text.format.DateFormat.format("HH:mm:ss",date).toString();
            if (trainingListener != null) {
                trainingListener.onTimeChange(time);
            }
            m_handler.postDelayed(handlerTimerTask, INTERVAL);
        }
    };
}
