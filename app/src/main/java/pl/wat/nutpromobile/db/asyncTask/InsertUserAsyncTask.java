package pl.wat.nutpromobile.db.asyncTask;

import android.os.AsyncTask;

import pl.wat.nutpromobile.db.dao.TrainingSummaryDao;
import pl.wat.nutpromobile.db.dao.UserDao;
import pl.wat.nutpromobile.db.row.TrainingSummaryRow;
import pl.wat.nutpromobile.db.row.UserRow;

public class InsertUserAsyncTask extends AsyncTask<UserRow, Void, Void> {

    private UserDao userDao;

    public InsertUserAsyncTask(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    protected Void doInBackground(UserRow... userRows) {
        userDao.insert(userRows[0]);
        return null;
    }
}
