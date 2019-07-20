package pl.wat.nutpromobile.di;

import dagger.Component;

import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import pl.wat.nutpromobile.NutproMobileApp;

@Component(modules = AndroidSupportInjectionModule.class)
public interface AppComponent extends AndroidInjector<NutproMobileApp> {
    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<NutproMobileApp>{}
}
