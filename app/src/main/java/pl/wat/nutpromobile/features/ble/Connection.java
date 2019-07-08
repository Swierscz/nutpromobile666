package pl.wat.nutpromobile.features.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import pl.wat.nutpromobile.R;

public class Connection implements LifecycleObserver {
    private final static String TAG = "Custom: " + Connection.class.getSimpleName();
    private Activity currentActivity;
    private BleScanner bleScanner;
    private BluetoothLeService bluetoothLeService;
    private CharacteristicManager characteristicManager;
    private String bluetoothAddress;
    private boolean isServiceConnected;
    private boolean isServiceBound = false;
    private Lifecycle lifecycle;

    public Connection(Activity activity, Lifecycle lifecycle) {
        currentActivity = activity;
        this.lifecycle = lifecycle;
        initConnection();
    }

    private BluetoothAdapter bluetoothAdapter;
    public BleScanner getBleScanner() {
        return bleScanner;
    }

    //region initialization
    public void initConnection() {
        initializeBluetoothAdapter();
        requestBluetooth();
        bleScanner = new BleScanner(bluetoothAdapter);
        currentActivity.registerReceiver(gattEventsReceiver, makeGattUpdateIntentFilter());
        characteristicManager = new CharacteristicManager();
        startServiceAsForeground();
        Log.i(TAG, "Connection initialization has finished");
    }

    private void initializeBluetoothAdapter() {
        BluetoothManager bluetoothManager =
                (BluetoothManager) currentActivity.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    private void requestBluetooth() {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            currentActivity.startActivityForResult(enableBtIntent, currentActivity.getResources().getInteger(R.integer.request_enable_bluetooth));
        }
    }

    private void startServiceAsForeground() {
        if (!BluetoothLeService.IS_STARTED) {
            currentActivity.startService(new Intent(currentActivity, BluetoothLeService.class));
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void bindBluetoothLeService() {
        currentActivity.registerReceiver(gattEventsReceiver, makeGattUpdateIntentFilter());
        Intent gattServiceIntent = new Intent(currentActivity, BluetoothLeService.class);
        currentActivity.bindService(gattServiceIntent, serviceConnection, 0);
        isServiceBound = true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void unBindBluetoothLeService() {
        if (isServiceBound) {
            currentActivity.unbindService(serviceConnection);
        }
        currentActivity.unregisterReceiver(gattEventsReceiver);

    }


    private final ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.i(TAG, BluetoothLeService.class.getSimpleName() + " connected");
            bluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!bluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                Log.e(TAG, "Bluetooth service cannot be started");
            }
            // Automatically connects to the device upon successful start-up initialization.
            isServiceConnected = true;
            characteristicManager.attachService(bluetoothLeService);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, BluetoothLeService.class.getSimpleName() + " disconnected");
            characteristicManager.detachService();
            isServiceConnected = false;
            bluetoothLeService = null;
        }
    };

    private final BroadcastReceiver gattEventsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                isServiceConnected = true;
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                isServiceConnected = false;
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.i(TAG, "Services discovered");
                characteristicManager.enableNotificationOnAllFromConfigFile();
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //BluetoothLeService.EXTRA_DATA
                String s = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                Log.i(TAG, "Received data is:\n " + s);
                Log.i(TAG, "Breakpoint");
            }
        }
    };


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public void connectToDevice(String aimBtAddress) {
        bluetoothAddress = aimBtAddress;
        if (bluetoothLeService != null) {
            Log.i(TAG, "Connection try started");
            final boolean result = bluetoothLeService.connect(bluetoothAddress);
            Log.i(TAG, "Connect request result=" + result);
        } else {
            Log.i(TAG, "Bluetooth service is not available");
        }
    }

    public void disconnectFromCurrentDevice() {
        bluetoothLeService.disconnect();
    }

    public void renewConnection() {
        currentActivity.registerReceiver(gattEventsReceiver, makeGattUpdateIntentFilter());
        if (bluetoothLeService != null) {
            final boolean result = bluetoothLeService.connect(bluetoothAddress);
            Log.i(TAG, "Connect request result=" + result);
        }
    }

    public void pauseConnection() {
        currentActivity.unregisterReceiver(gattEventsReceiver);
    }

    public void sendCommand(String command) {
        characteristicManager.writeCharacteristicByUuid("0000ffe1-0000-1000-8000-00805f9b34fb", command);
    }
}