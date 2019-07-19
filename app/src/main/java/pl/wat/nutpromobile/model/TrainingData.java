package pl.wat.nutpromobile.model;

import android.location.Location;

import lombok.AllArgsConstructor;
import lombok.Data;

public class TrainingData {
    private SensoricData sensoricData;
    private Location location;
    private float distance;

    public TrainingData(SensoricData sensoricData, Location location, float distance) {
        this.sensoricData = sensoricData;
        this.location = location;
        this.distance = distance;
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

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
