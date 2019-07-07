package pl.wat.nutpromobile.training;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import pl.wat.nutpromobile.NotificationCreator;
import pl.wat.nutpromobile.ble.BluetoothLeService;
import pl.wat.nutpromobile.ble.Connection;

public class TrainingService extends Service {

    public static String NOTIFICATION = "pl_wat_nutpro_mobile_notification_icon";

    private static final int TRAINING_SERVICE_ID = 10201;

    private String staticFileName;

    private String storagePath;

    private BufferedWriter bufferedWriter;

    private IBinder mBinder;

    private Context context;

    private Connection connection;

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
        mBinder = new LocalBinder();
        context = getBaseContext();
        Notification notification = intent.getExtras().getParcelable(NOTIFICATION);
        startForeground(NotificationCreator.getNotificationId(), NotificationCreator.getNotification(getApplicationContext()));
        return START_STICKY;
    }

    public void handleTraining(Connection connection) {
        if (this.connection != null)
            return;
        this.connection = connection;
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
        getApplicationContext().registerReceiver(gattEventsReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getApplicationContext().unregisterReceiver(gattEventsReceiver);
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public class LocalBinder extends Binder {
        public TrainingService getService() {
            //zwracamy instancje serwisu, przez nią odwołamy się następnie do metod.
            return TrainingService.this;
        }
    }

    private final BroadcastReceiver gattEventsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //BluetoothLeService.EXTRA_DATA
                String s = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                try {
                    bufferedWriter.append("Received data is: " + s + " Breakpoint\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
