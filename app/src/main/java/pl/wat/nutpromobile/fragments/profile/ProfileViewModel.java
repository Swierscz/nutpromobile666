package pl.wat.nutpromobile.fragments.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import pl.wat.nutpromobile.NutproMobileApp;
import pl.wat.nutpromobile.db.repository.UserRepository;
import pl.wat.nutpromobile.db.row.UserRow;

public class ProfileViewModel extends AndroidViewModel {

    private UserRepository userRepository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        userRepository = ((NutproMobileApp)application).repositoryProvider
                .getUserRepositoryInstance((NutproMobileApp)application);
    }

    public LiveData<UserRow> getUser() {
        return userRepository.getUser();
    }
}
