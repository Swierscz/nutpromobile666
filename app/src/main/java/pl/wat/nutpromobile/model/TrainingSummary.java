package pl.wat.nutpromobile.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrainingSummary {
    private Date startTrainingTime;
    private Date stopTrainingTime;

}
