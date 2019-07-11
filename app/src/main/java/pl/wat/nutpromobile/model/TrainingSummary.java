package pl.wat.nutpromobile.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingSummary {
    private Date startTrainingTime;
    private Date stopTrainingTime;

}
