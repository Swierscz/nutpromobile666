package pl.wat.nutpromobile.fragments.trainingSummary;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import pl.wat.nutpromobile.NutproMobileApp;
import pl.wat.nutpromobile.db.repository.TrainingSummaryRepository;
import pl.wat.nutpromobile.db.row.TrainingSummaryRow;
import pl.wat.nutpromobile.model.TrainingSummary;

public class TrainingSummaryViewModel extends AndroidViewModel {

    private TrainingSummaryRepository trainingSummaryRepository;

    private TrainingSummaryRow trainingSummary;

    public TrainingSummaryViewModel(@NonNull Application application) {
        super(application);
        trainingSummaryRepository = ((NutproMobileApp)application).repositoryProvider
                .getTrainingSummaryRepositoryInstance((NutproMobileApp)application);
    }

    public LiveData<TrainingSummaryRow> getTrainingSummary(int id) {
        return trainingSummaryRepository.getTrainingSummary(id);
    }
}
