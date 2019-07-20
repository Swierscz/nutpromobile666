package pl.wat.nutpromobile.activity.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.wat.nutpromobile.NutproMobileApp;
import pl.wat.nutpromobile.R;
import pl.wat.nutpromobile.di.components.ActivityComponent;
import pl.wat.nutpromobile.di.components.DaggerActivityComponent;
import pl.wat.nutpromobile.di.modules.ActivityModule;
import pl.wat.nutpromobile.features.ble.BluetoothConnection;
import pl.wat.nutpromobile.features.location.UserLocation;
import pl.wat.nutpromobile.features.service.MyNotification;
import pl.wat.nutpromobile.features.training.Training;
import pl.wat.nutpromobile.fragments.connection.OnConnectionFragmentInteractionListener;
import pl.wat.nutpromobile.fragments.training.OnTrainingFragmentInteractionListener;

public class MainActivity extends AppCompatActivity
        implements OnConnectionFragmentInteractionListener
        , OnTrainingFragmentInteractionListener
        , SharedPreferences.OnSharedPreferenceChangeListener {
    public final static String TAG = "Custom: " + MainActivity.class.getSimpleName();
    public static final String INTENT_FIRST_LAUNCH = "pl.wat.nutpromobile.MainActivity.FIRST_LAUNCH";

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @Inject BluetoothConnection bluetoothConnection;
    @Inject UserLocation userLocation;
    @Inject Permission permission;
    @Inject PreferencesManager preferencesManager;
    @Inject Training training;
    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, TAG + " created");
        setContentView(R.layout.activity_main);

        activityComponent = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((NutproMobileApp)getApplication())
                        .getApplicationComponent())
                .build();
        activityComponent.inject(this);

        permission.startPermissionRequest();
        ButterKnife.bind(this);
        setupNavigation();
        addLifecycleObservers();
        MyNotification.createNotificationChannel(this);
    }


    public void addLifecycleObservers(){
        getLifecycle().addObserver(userLocation);
        getLifecycle().addObserver(bluetoothConnection);
        getLifecycle().addObserver(training);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, TAG + " started");
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public BluetoothConnection getBluetoothConnection() {
        return bluetoothConnection;
    }


    private void setupNavigation() {
        NavigationUI.setupWithNavController(navigation, Navigation.findNavController(this, R.id.nav_host_fragment));
        checkIfIsFirstLaunchAndNavigate();
    }

    private void checkIfIsFirstLaunchAndNavigate() {
        Intent intent = getIntent();
        boolean firstLunch = intent.getBooleanExtra(MainActivity.INTENT_FIRST_LAUNCH, true);
        if (firstLunch) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.bluetoothConnection);
            intent.removeExtra(MainActivity.INTENT_FIRST_LAUNCH);
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, TAG + " destroyed");
        super.onDestroy();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permission.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        preferencesManager.setChangePreferencesBehaviours(sharedPreferences, key);
    }


    @Override
    public Training getTraining() {
        return training;
    }

}
