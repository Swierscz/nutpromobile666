package pl.wat.nutpromobile.model;

import android.location.Location;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrainingData {
    private SensoricData sensoricData;
    private Location location;

}
