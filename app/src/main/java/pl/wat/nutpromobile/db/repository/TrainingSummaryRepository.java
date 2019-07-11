package pl.wat.nutpromobile.db.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import pl.wat.nutpromobile.NutproMobileApp;
import pl.wat.nutpromobile.db.AppDatabase;
import pl.wat.nutpromobile.db.asyncTask.InsertTrainingSummaryAsyncTask;
import pl.wat.nutpromobile.db.dao.TrainingSummaryDao;
import pl.wat.nutpromobile.db.row.TrainingSummaryRow;
import pl.wat.nutpromobile.model.TrainingSummary;

public class TrainingSummaryRepository {

    private TrainingSummaryDao trainingSummaryDao;
    private LiveData<List<TrainingSummaryRow>> trainingSummaryRowList;

    public TrainingSummaryRepository(NutproMobileApp application) {
        AppDatabase db = application.getDatabaseInstance();
        trainingSummaryDao = db.trainingSummaryDao();
        trainingSummaryRowList = trainingSummaryDao.getAll();
    }

    public LiveData<List<TrainingSummaryRow>> getAllTrainings() {
        return trainingSummaryRowList;
    }

    public void insertTrainingSummary(TrainingSummaryRow trainingSummaryRow) {
        new InsertTrainingSummaryAsyncTask(trainingSummaryDao).execute(trainingSummaryRow);
    }

    public LiveData<TrainingSummaryRow> getTrainingSummary(int id) {
        return trainingSummaryDao.findTrainingSummaryById(id);
    }
}
