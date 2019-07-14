package pl.wat.nutpromobile.db.repository;

import androidx.lifecycle.LiveData;

import pl.wat.nutpromobile.NutproMobileApp;
import pl.wat.nutpromobile.db.AppDatabase;
import pl.wat.nutpromobile.db.asyncTask.InsertUserAsyncTask;
import pl.wat.nutpromobile.db.dao.UserDao;
import pl.wat.nutpromobile.db.row.UserRow;
import pl.wat.nutpromobile.model.User;

public class UserRepository {

    private UserDao userDao;

    public UserRepository(NutproMobileApp application) {
        AppDatabase db = application.getDatabaseInstance();
        userDao = db.userDao();
    }

    public void insertUser(UserRow userRow) {
        new InsertUserAsyncTask(userDao).execute(userRow);
    }

    public LiveData<UserRow> getUser() {
        return userDao.getUser();
    }
}
