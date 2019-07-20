package pl.wat.nutpromobile.di.components;

import dagger.Component;
import pl.wat.nutpromobile.NutproMobileApp;
import pl.wat.nutpromobile.di.ApplicationScope;
import pl.wat.nutpromobile.di.modules.ApplicationModule;

@ApplicationScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(NutproMobileApp nutproMobileApp);
}
