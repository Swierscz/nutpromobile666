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

import javax.inject.Inject;

import pl.wat.nutpromobile.R;
import pl.wat.nutpromobile.activity.main.MainActivity;
import pl.wat.nutpromobile.features.service.ServiceManager;

public class BluetoothConnection implements LifecycleObserver, ServiceManager {
    private final static String TAG = "Custom: " + BluetoothConnection.class.getSimpleName();
    private MainActivity currentActivity;
    private BleScanner bleScanner;
    private BluetoothLeService bluetoothLeService;
    private CharacteristicManager characteristicManager;
    private String bluetoothAddress;
    private Lifecycle lifecycle;

    @Inject
    public BluetoothConnection(MainActivity activity, Lifecycle lifecycle) {
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
        Log.i(TAG, "BluetoothConnection initialization has finished");
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

    private boolean shouldUnbindBluetoothService;


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    @Override
    public void release() {
        unbindService();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @Override
    public void resume() {
        registerReceivers();
        bindService();
    }

    @Override
    public void bindService() {
        Intent gattServiceIntent = new Intent(currentActivity, BluetoothLeService.class);
        currentActivity.bindService(gattServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        shouldUnbindBluetoothService = true;
    }

    @Override
    public void unbindService() {
        if (shouldUnbindBluetoothService) {
            currentActivity.unbindService(serviceConnection);
            shouldUnbindBluetoothService = false;
        }
        currentActivity.unregisterReceiver(gattEventsReceiver);
    }



    public void stopService(){
        currentActivity.stopService(new Intent(currentActivity, BluetoothLeService.class));
    }

    private void registerReceivers() {
        currentActivity.registerReceiver(gattEventsReceiver, makeGattUpdateIntentFilter());
    }

    private void unregisterReceivers() {
        currentActivity.unregisterReceiver(gattEventsReceiver);
    }


    private final ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!bluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                Log.e(TAG, "Bluetooth service cannot be started");
            }
            // Automatically connects to the device upon successful start-up initialization.
            shouldUnbindBluetoothService = true;
            characteristicManager.attachService(bluetoothLeService);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            characteristicManager.detachService();
            shouldUnbindBluetoothService = false;
            bluetoothLeService = null;
        }
    };

    private final BroadcastReceiver gattEventsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.i(TAG, "Services discovered");
                characteristicManager.enableNotificationOnAllFromConfigFile();
            }
        }
    };


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        return intentFilter;
    }

    public void connectToDevice(String aimBtAddress) {
        bluetoothAddress = aimBtAddress;
        if (bluetoothLeService != null) {
            Log.i(TAG, "BluetoothConnection try started");
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

    public void addConnectionListener(BluetoothConnectionListener bluetoothConnectionListener) {
        bluetoothLeService.addConnectionListener(bluetoothConnectionListener);
    }

    public void removeConnectionListener() {
        bluetoothLeService.removeConnectionListener();
    }

    public void pauseConnection() {
        currentActivity.unregisterReceiver(gattEventsReceiver);
    }

    public void sendCommand(String command) {
        characteristicManager.writeCharacteristicByUuid("0000ffe1-0000-1000-8000-00805f9b34fb", command);
    }
}