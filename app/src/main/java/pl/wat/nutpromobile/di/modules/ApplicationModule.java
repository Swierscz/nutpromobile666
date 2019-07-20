package pl.wat.nutpromobile.di.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import pl.wat.nutpromobile.di.ActivityScope;
import pl.wat.nutpromobile.di.ApplicationContext;
import pl.wat.nutpromobile.di.ApplicationScope;

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Application application(){return application;}

    @Provides
    @ApplicationContext
    Context context(){return application;}

    @Provides
    SharedPreferences sharedPreferences(){
        return application.getSharedPreferences("default", Context.MODE_PRIVATE);
    }



}
