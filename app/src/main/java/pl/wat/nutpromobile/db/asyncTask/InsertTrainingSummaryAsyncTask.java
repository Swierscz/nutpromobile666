package pl.wat.nutpromobile.db.asyncTask;

import android.os.AsyncTask;

import pl.wat.nutpromobile.db.dao.TrainingSummaryDao;
import pl.wat.nutpromobile.db.row.TrainingSummaryRow;

public class InsertTrainingSummaryAsyncTask extends AsyncTask<TrainingSummaryRow, Void, Void> {

    private TrainingSummaryDao trainingSummaryDao;

    public InsertTrainingSummaryAsyncTask(TrainingSummaryDao trainingSummaryDao) {
        this.trainingSummaryDao = trainingSummaryDao;
    }

    @Override
    protected Void doInBackground(TrainingSummaryRow... trainingSummaryRows) {
        trainingSummaryDao.insert(trainingSummaryRows[0]);
        return null;
    }
}
