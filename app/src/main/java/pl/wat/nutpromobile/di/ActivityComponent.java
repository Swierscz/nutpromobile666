package pl.wat.nutpromobile.di;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import pl.wat.nutpromobile.activity.main.MainActivity;

@Subcomponent(modules = MainActivityModule.class)
public interface ActivityComponent extends AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity>{}
}
