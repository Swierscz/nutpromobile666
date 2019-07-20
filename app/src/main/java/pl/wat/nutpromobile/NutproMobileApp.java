package pl.wat.nutpromobile;

import android.app.Application;

import androidx.room.Room;

import pl.wat.nutpromobile.db.AppDatabase;
import pl.wat.nutpromobile.db.repository.RepositoryProvider;
import pl.wat.nutpromobile.di.components.ApplicationComponent;
import pl.wat.nutpromobile.di.components.DaggerApplicationComponent;


public class NutproMobileApp extends Application{

    private AppDatabase db;
    ApplicationComponent applicationComponent;

    // TODO Use daggera
    public RepositoryProvider repositoryProvider;

    public NutproMobileApp() {
        repositoryProvider = new RepositoryProvider();
    }

    public AppDatabase getDatabaseInstance() {
        if (db == null) {Dagge
            db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "nutpro-db").build();
        }
        return db;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent
                .builder()
                .build();
        applicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
