package pl.wat.nutpromobile;

import android.app.Application;

import androidx.room.Room;

import pl.wat.nutpromobile.db.AppDatabase;
import pl.wat.nutpromobile.db.repository.RepositoryProvider;

public class NutproMobileApp extends Application {

    private AppDatabase db;

    // TODO Use daggera
    public RepositoryProvider repositoryProvider;

    public NutproMobileApp() {
        repositoryProvider = new RepositoryProvider();
    }

    public AppDatabase getDatabaseInstance() {
        if (db == null) {
            db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "nutpro-db").build();
        }
        return db;
    }
}
