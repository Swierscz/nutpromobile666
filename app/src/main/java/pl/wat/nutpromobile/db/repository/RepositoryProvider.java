package pl.wat.nutpromobile.db.repository;

import pl.wat.nutpromobile.NutproMobileApp;

public class RepositoryProvider {
    private TrainingSummaryRepository trainingSummaryRepository;
    private UserRepository userRepository;

    public TrainingSummaryRepository getTrainingSummaryRepositoryInstance(NutproMobileApp nutproMobileApp) {
        if (trainingSummaryRepository == null) {
            trainingSummaryRepository = new TrainingSummaryRepository(nutproMobileApp);
        }
        return trainingSummaryRepository;
    }

    public UserRepository getUserRepositoryInstance(NutproMobileApp nutproMobileApp) {
        if (userRepository == null) {
            userRepository = new UserRepository(nutproMobileApp);
        }
        return userRepository;
    }
}
