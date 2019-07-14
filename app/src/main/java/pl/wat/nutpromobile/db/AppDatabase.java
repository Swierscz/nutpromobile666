package pl.wat.nutpromobile.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import pl.wat.nutpromobile.db.dao.TrainingSummaryDao;
import pl.wat.nutpromobile.db.dao.UserDao;
import pl.wat.nutpromobile.db.row.TrainingSummaryRow;
import pl.wat.nutpromobile.db.row.UserRow;

@Database(entities = {TrainingSummaryRow.class, UserRow.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TrainingSummaryDao trainingSummaryDao();
    public abstract UserDao userDao();
}
