package pl.wat.nutpromobile.ble;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.util.List;
import java.util.UUID;

public class CharacteristicManager {
    public final static String TAG = CharacteristicManager.class.getSimpleName();
    private BluetoothGattCharacteristic currentNotifyCharacteristic = null;
    private BluetoothLeService bluetoothLeService;


    public void attachService(BluetoothLeService bluetoothLeService){
        this.bluetoothLeService = bluetoothLeService;
    }

    public void detachService(){
        this.bluetoothLeService = null;
    }

    public void writeCharacteristicByUuid(String uuid, String data) {
        bluetoothLeService.writeCharacteristic(searchForCharacteristicByUuid(uuid), data);
    }

    public void enableNotificationOnAllFromConfigFile(){
        enableNotificationForCharacteristicsByUuid(BleAttributes.SUPPORTED_CHARACTERISTICS);
    }

    private void enableNotificationForCharacteristicsByUuid(String... uuids) {
        for (String uuid : uuids){
            enableNotificationForSingleCharacteristic(searchForCharacteristicByUuid(uuid), true);
        }
    }

    private BluetoothGattCharacteristic searchForCharacteristicByUuid(String uuidString) {
        UUID uuid = null;
        BluetoothGattCharacteristic foundCharacteristic = null;
        try {
            uuid = UUID.fromString(uuidString);
            if(bluetoothLeService != null) {
                List<BluetoothGattService> services = bluetoothLeService.getSupportedGattServices();
                if (services != null && services.size() != 0) {
                    Log.i(TAG, "Search for charecteristic started");
                    for (BluetoothGattService service : services) {
                        BluetoothGattCharacteristic tempChar = service.getCharacteristic(uuid);
                        if (tempChar != null) {
                            foundCharacteristic = tempChar;
                        }
                    }
                }
            }
        }catch (IllegalArgumentException e){
            Log.i(TAG, "Cannot transform provided string to uuid");
        }

        if(foundCharacteristic == null) Log.i(TAG, "Cannot find characteristic by uuid: " + uuidString);
        return foundCharacteristic;
    }

    public void enableNotificationForSingleCharacteristic(BluetoothGattCharacteristic characteristic, Boolean enabled) {
        if (characteristic != null) {
            final int charaProp = characteristic.getProperties();
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                // If there is an active notification on a characteristic, clear
                // it first so it doesn't update the data field on the user interface.
                if (currentNotifyCharacteristic != null) {
                    bluetoothLeService.setCharacteristicNotification(
                            currentNotifyCharacteristic, false);
                    currentNotifyCharacteristic = null;
                }
                bluetoothLeService.readCharacteristic(characteristic);
            }
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                currentNotifyCharacteristic = characteristic;
                bluetoothLeService.setCharacteristicNotification(
                        characteristic, enabled);
            }
        }
    }



}
