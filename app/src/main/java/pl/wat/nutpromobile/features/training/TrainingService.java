package pl.wat.nutpromobile.features.training;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Environment;
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
import pl.wat.nutpromobile.util.NotificationCreator;

public class TrainingService extends Service implements UserLocationListener, ConnectionListener {
    private final static String TAG = TrainingService.class.getSimpleName();

    private String staticFileName;

    private String storagePath;

    private BufferedWriter bufferedWriter;

    private IBinder mBinder;

    private Context context;

    private Connection connection;

    private UserLocation userLocation;

    private TrainingListener trainingListener;

    private Location location;

    private float distance;

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
            startForeground(NotificationCreator.getNotificationId(), NotificationCreator.getNotification(getApplicationContext()));
        }
        distance = 0;
        return START_STICKY;
    }

    public void handleTraining(Connection connection, UserLocation userLocation) {
        if (this.connection != null || this.userLocation != null)
            return;
        this.connection = connection;
        this.userLocation = userLocation;
        userLocation.addUserLocationListener(this);
        connection.addConnectionListener(this);
        connection.sendCommand("a");
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

    @Override
    public void onUserLocationChanged(Location location) {
        if (this.location != null) {
            distance = distance + this.location.distanceTo(location);
        }
        this.location = location;
        System.out.println("Loc: " + location.getLatitude() + " - " + location.getLongitude());
        trainingListener.onTrainingDataProcessed(new TrainingData(null, location, distance));
    }

    @Override
    public void onDataReceived(SensoricData sensoricData) {
        trainingListener.onTrainingDataProcessed(new TrainingData(sensoricData, location, distance));
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

    public void addTrainingListener(TrainingListener trainingListener) {
        this.trainingListener = trainingListener;
    }

    public void removeTrainingListener() {
        this.trainingListener = null;
    }

    private void prepareServiceToStop() {
        connection.sendCommand("q");
        userLocation.removeUserLocationListener();
        connection.removeConnectionListener();
    }
}
