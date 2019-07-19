package pl.wat.nutpromobile.model;

import lombok.AllArgsConstructor;
import lombok.Data;


public class SensoricData {
    private String rawData;

    public SensoricData(String rawData) {
        this.rawData = rawData;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }
}
