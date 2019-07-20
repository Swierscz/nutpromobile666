package pl.wat.nutpromobile.di;

import dagger.Module;
import dagger.Provides;
import pl.wat.nutpromobile.activity.main.MainActivity;
import pl.wat.nutpromobile.activity.main.MainActivityViewModel;

@Module
class MainActivityModule {
    
    @Provides
    MainActivityViewModel provideMainActivityViewModel(){
        return new MainActivityViewModel();
    }
    
}
