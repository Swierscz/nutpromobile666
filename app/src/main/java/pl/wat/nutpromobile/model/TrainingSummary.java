package pl.wat.nutpromobile.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingSummary {

    public int tsrid;

    private String trainingType;

    private Date startTrainingTime;

    private Date stopTrainingTime;

    private float distance;

    private float averageSpeed;

}
