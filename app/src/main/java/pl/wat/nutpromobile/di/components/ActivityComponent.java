package pl.wat.nutpromobile.di.components;

import dagger.Component;
import pl.wat.nutpromobile.activity.main.MainActivity;
import pl.wat.nutpromobile.di.ActivityScope;
import pl.wat.nutpromobile.di.modules.ActivityModule;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
}
