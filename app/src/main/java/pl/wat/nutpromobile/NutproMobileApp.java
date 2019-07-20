package pl.wat.nutpromobile;

import android.app.Application;

import androidx.room.Room;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.internal.DaggerCollections;
import pl.wat.nutpromobile.db.AppDatabase;
import pl.wat.nutpromobile.db.repository.RepositoryProvider;
import pl.wat.nutpromobile.di.DaggerAppComponent;

public class NutproMobileApp extends DaggerApplication {

    private AppDatabase db;

    // TODO Use daggera
    public RepositoryProvider repositoryProvider;

    public NutproMobileApp() {
        repositoryProvider = new RepositoryProvider();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }

    public AppDatabase getDatabaseInstance() {
        if (db == null) {
            db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "nutpro-db").build();
        }
        return db;
    }
}
