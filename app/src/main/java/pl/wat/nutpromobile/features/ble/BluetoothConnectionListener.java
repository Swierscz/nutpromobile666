package pl.wat.nutpromobile.features.ble;

import pl.wat.nutpromobile.model.SensoricData;

public interface BluetoothConnectionListener {
    void onDataReceived(SensoricData sensoricData);
}
