package pl.wat.nutpromobile.fragments.trainingsHistory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import pl.wat.nutpromobile.NutproMobileApp;
import pl.wat.nutpromobile.db.repository.TrainingSummaryRepository;
import pl.wat.nutpromobile.db.row.TrainingSummaryRow;

public class TrainingHistoryViewModel extends AndroidViewModel {

    private TrainingSummaryRepository trainingSummaryRepository;

    private LiveData<List<TrainingSummaryRow>> trainingSummaryList;

    public TrainingHistoryViewModel(@NonNull Application application) {
        super(application);
        trainingSummaryRepository = ((NutproMobileApp)application).repositoryProvider
                .getTrainingSummaryRepositoryInstance((NutproMobileApp)application);
        trainingSummaryList = trainingSummaryRepository.getAllTrainings();
    }

    public LiveData<List<TrainingSummaryRow>> getAllTrainings() {
        return trainingSummaryList;
    }

    public void insert(TrainingSummaryRow trainingSummaryRow) {
        trainingSummaryRepository.insertTrainingSummary(trainingSummaryRow);
    }
}
