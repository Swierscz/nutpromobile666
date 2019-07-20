package pl.wat.nutpromobile.di;

import dagger.android.ContributesAndroidInjector;
import pl.wat.nutpromobile.activity.login.LoginActivity;
import pl.wat.nutpromobile.activity.main.MainActivity;

public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = LoginActivityModule.class)
    abstract LoginActivity contributeLoginActivity();

    @ContributesAndroidInjector(modules = {
            MainActivityModule.class

    })
    abstract MainActivity contributeMainActivity();
}
