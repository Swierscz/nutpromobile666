package pl.wat.nutpromobile.features.ble;

import pl.wat.nutpromobile.model.SensoricData;

public interface ConnectionListener {
    void onDataReceived(SensoricData sensoricData);
}
