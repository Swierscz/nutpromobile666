package pl.wat.nutpromobile;

import android.app.Application;

import androidx.room.Room;

import pl.wat.nutpromobile.db.AppDatabase;

public class NutproMobileApp extends Application {

    private AppDatabase db;

    public AppDatabase getDatabaseInstance() {
        if (db == null) {
            db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "nutpro-db").build();
        }
        return db;
    }
}
