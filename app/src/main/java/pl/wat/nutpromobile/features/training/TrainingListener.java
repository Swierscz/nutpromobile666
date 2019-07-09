package pl.wat.nutpromobile.features.training;

import pl.wat.nutpromobile.model.TrainingData;

public interface TrainingListener {
    void onTrainingDataProcessed(TrainingData trainingData);
}
