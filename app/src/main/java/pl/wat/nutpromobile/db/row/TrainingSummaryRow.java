package pl.wat.nutpromobile.db.row;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Entity
@Data
public class TrainingSummaryRow implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int tsrid;

    @ColumnInfo(name = "training_type")
    private String trainingType;

    @ColumnInfo(name = "start_training_time")
    private String startTrainingTime;

    @ColumnInfo(name = "stop_training_time")
    private String stopTrainingTime;

    @ColumnInfo(name = "distance")
    private float distance;

    @ColumnInfo(name = "average_speed")
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

    public String getStartTrainingTime() {
        return startTrainingTime;
    }

    public void setStartTrainingTime(String startTrainingTime) {
        this.startTrainingTime = startTrainingTime;
    }

    public String getStopTrainingTime() {
        return stopTrainingTime;
    }

    public void setStopTrainingTime(String stopTrainingTime) {
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
