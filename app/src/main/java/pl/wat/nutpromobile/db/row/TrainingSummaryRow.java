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

    @ColumnInfo(name = "start_training_time")
    private String startTrainingTime;

    @ColumnInfo(name = "stop_training_time")
    private String stopTrainingTime;
}
