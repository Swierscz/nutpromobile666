package pl.wat.nutpromobile.di.modules;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.Lifecycle;

import dagger.Module;
import dagger.Provides;
import pl.wat.nutpromobile.activity.main.MainActivity;
import pl.wat.nutpromobile.di.ActivityContext;
import pl.wat.nutpromobile.di.ActivityScope;

@Module
public class ActivityModule {

    private MainActivity activity;

    public ActivityModule(MainActivity activity) {
        this.activity = activity;
    }

    @ActivityScope
    Context context(){return activity;}

    @Provides
    MainActivity mainActivity(){return activity;}

    @Provides
    Lifecycle lifecycle(){return activity.getLifecycle();}


}
