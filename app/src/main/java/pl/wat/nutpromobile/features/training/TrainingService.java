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
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import pl.wat.nutpromobile.features.ble.BluetoothConnection;
import pl.wat.nutpromobile.features.ble.BluetoothConnectionListener;
import pl.wat.nutpromobile.features.location.UserLocation;
import pl.wat.nutpromobile.features.location.UserLocationListener;
import pl.wat.nutpromobile.model.SensoricData;
import pl.wat.nutpromobile.model.TrainingData;
import pl.wat.nutpromobile.features.service.MyNotification;

public class TrainingService extends Service implements UserLocationListener, BluetoothConnectionListener {
    private final static String TAG = TrainingService.class.getSimpleName();

    private String staticFileName;

    private String storagePath;

    private BufferedWriter bufferedWriter;

    private IBinder mBinder;

    private Context context;

    private BluetoothConnection bluetoothConnection;

    private UserLocation userLocation;

    private TrainingListener trainingListener;

    private Location location;

    @Override
    public void onCreate() {
        super.onCreate();
        staticFileName = "sensorData";
        storagePath = Environment.getExternalStorageDirectory().getPath();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, TAG + " binded");
        return mBinder;
    }
    private boolean shouldWork = true;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int i = ii++;
//                Log.i(TAG, "::: " + (i));
                if(shouldWork) {
                    handler.postDelayed(this, 1000);
                    MyNotification.updateText(TrainingService.this, Integer.toString(i));
                    Intent intent1 = new Intent();
                    intent1.setAction("test");
                    intent1.putExtra("test1", Integer.toString(i));
                    sendBroadcast(intent1);
                }
            }
        };

        if(intent!=null) {
            if (MyNotification.Action.PAUSE.toString().equals(intent.getAction())) {
                Log.i(TAG, "Pause training triggered");
                MyNotification.changeNotificationToResumeButton(context);
            } else if (MyNotification.Action.RESUME.toString().equals(intent.getAction())) {
                Log.i(TAG, "Resume training triggered");
                MyNotification.changeNotificationToPauseButton(context);
            } else if (MyNotification.Action.END.toString().equals(intent.getAction())) {
                Log.i(TAG, "End training triggered");
                shouldWork = false;
                handler.removeCallbacks(runnable);
                stopSelf();
            } else {
                handler.postDelayed(runnable, 1000);
                Log.i(TAG, TAG + " foreground started");
                mBinder = new LocalBinder();
                context = getBaseContext();
                startForeground(MyNotification.getNotificationId(), MyNotification.getNotification(getApplicationContext(), true));
            }
        }



        return START_STICKY;
    }

    private int ii = 0;

    public void handleTraining(BluetoothConnection bluetoothConnection, UserLocation userLocation) {
        if (this.bluetoothConnection != null || this.userLocation != null)
            return;
        this.bluetoothConnection = bluetoothConnection;
        this.userLocation = userLocation;
        userLocation.addUserLocationListener(this);
        bluetoothConnection.addConnectionListener(this);
        bluetoothConnection.sendCommand("a");
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
        if(bufferedWriter!=null) {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    @Override
    public void onUserLocationChanged(Location location) {
        System.out.println("Loc: " + location.getLatitude() + " - " + location.getLongitude());
    }

    @Override
    public void onDataReceived(SensoricData sensoricData) {
        trainingListener.onTrainingDataProcessed(new TrainingData(sensoricData, location));
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
        if(bluetoothConnection!=null){
            bluetoothConnection.sendCommand("q");
            bluetoothConnection.removeConnectionListener();
        }

        if(userLocation!=null)
            userLocation.removeUserLocationListener();
    }
}
