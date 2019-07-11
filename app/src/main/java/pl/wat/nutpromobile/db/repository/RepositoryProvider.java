package pl.wat.nutpromobile.db.repository;

import pl.wat.nutpromobile.NutproMobileApp;

public class RepositoryProvider {
    private TrainingSummaryRepository trainingSummaryRepository;

    public TrainingSummaryRepository getTrainingSummaryRepositoryInstance(NutproMobileApp nutproMobileApp) {
        if (trainingSummaryRepository == null) {
            trainingSummaryRepository = new TrainingSummaryRepository(nutproMobileApp);
        }
        return trainingSummaryRepository;
    }
}
