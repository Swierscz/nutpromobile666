package pl.wat.nutpromobile.model;

import android.location.Location;

public class TrainingData {
    private SensoricData sensoricData;
    private Location location;

    public TrainingData(SensoricData sensoricData, Location location) {
        this.sensoricData = sensoricData;
        this.location = location;
    }

    public SensoricData getSensoricData() {
        return sensoricData;
    }

    public void setSensoricData(SensoricData sensoricData) {
        this.sensoricData = sensoricData;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
