package pl.wat.nutpromobile.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class TrainingSummary {

    public TrainingSummary() {

    }

    public TrainingSummary(int tsrid, String trainingType, Date startTrainingTime, Date stopTrainingTime, float distance, float averageSpeed) {
        this.tsrid = tsrid;
        this.trainingType = trainingType;
        this.startTrainingTime = startTrainingTime;
        this.stopTrainingTime = stopTrainingTime;
        this.distance = distance;
        this.averageSpeed = averageSpeed;
    }

    public int tsrid;

    private String trainingType;

    private Date startTrainingTime;

    private Date stopTrainingTime;

    private float distance;

    private float averageSpeed;


    public int getTsrid() {
        return tsrid;
    }

    public void setTsrid(int tsrid) {
        this.tsrid = tsrid;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public Date getStartTrainingTime() {
        return startTrainingTime;
    }

    public void setStartTrainingTime(Date startTrainingTime) {
        this.startTrainingTime = startTrainingTime;
    }

    public Date getStopTrainingTime() {
        return stopTrainingTime;
    }

    public void setStopTrainingTime(Date stopTrainingTime) {
        this.stopTrainingTime = stopTrainingTime;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(float averageSpeed) {
        this.averageSpeed = averageSpeed;
    }
}
