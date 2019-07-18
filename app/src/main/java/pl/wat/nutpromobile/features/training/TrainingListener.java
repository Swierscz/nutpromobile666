package pl.wat.nutpromobile.features.training;

import pl.wat.nutpromobile.model.TrainingData;

public interface TrainingListener {
    void onTrainingDataProcessed(TrainingData trainingData);
    void onTimeChange(String time);
    void onDistanceChange(String distance);
    void onSpeedChange(String speed);
}
