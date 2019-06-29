package pl.wat.nutpromobile.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pl.wat.nutpromobile.model.BluetoothDevice;


public class BleScanner {
    private final static String TAG = BleScanner.class.getSimpleName();

    public BleScanner(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
    }

    private BluetoothAdapter bluetoothAdapter;
    private Handler handler = new Handler();
    final private long scanPeriod = 10000;
    private final BluetoothLeScanner bluetoothLeScanner;
    public volatile boolean isScanning = false;

    private List<BluetoothDevice> listOfDevices = new ArrayList<BluetoothDevice>();

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            super.onScanResult(callbackType, result);

            boolean isAlreadyOnList = false;

            for(BluetoothDevice bluetoothDevice : listOfDevices){
                if(bluetoothDevice.getAddress().equals(result.getDevice().getAddress())){
                    isAlreadyOnList = true;
                }
            }

            if(!isAlreadyOnList){
                listOfDevices.add(new BluetoothDevice(result.getDevice().getName(), result.getDevice().getAddress()));
                Log.i(TAG, "Found device: " + result.getDevice().getName());
            }



            //TODO Dorobić liste do extinctow (rzuca cały czas wartosciami)
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.i(TAG, "Scan failed");
        }
    };

    void startDeviceScan(){
        scanLeDevice(true);
    }

    void stopDeviceScan(){
        scanLeDevice(false);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            startScanForSpecifiedPeriod(scanPeriod);
        } else {
            stopScan();
        }
    }

    public List<BluetoothDevice> scanForDevices(){
        startDeviceScan();
        while(isScanning);
        return listOfDevices;
    }

    private void startScanForSpecifiedPeriod(long scanPeriod) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopScan();
            }
        }, scanPeriod);
        listOfDevices.clear();
        isScanning = true;
        bluetoothLeScanner.startScan(scanCallback);
        Log.i(TAG, "Device scan started");
    }

    private void stopScan() {
        isScanning = false;
        bluetoothLeScanner.stopScan(scanCallback);
        Log.i(TAG, "Device scan stopped");
    }

    public List<BluetoothDevice> getListOfDevices() {
        return listOfDevices;
    }
}
