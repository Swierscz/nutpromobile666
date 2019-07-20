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
import pl.wat.nutpromobile.R;
import pl.wat.nutpromobile.di.BaseActivity;
import pl.wat.nutpromobile.features.ble.BluetoothConnection;
import pl.wat.nutpromobile.features.location.UserLocation;
import pl.wat.nutpromobile.features.service.MyNotification;
import pl.wat.nutpromobile.features.training.Training;
import pl.wat.nutpromobile.fragments.connection.OnConnectionFragmentInteractionListener;
import pl.wat.nutpromobile.fragments.training.OnTrainingFragmentInteractionListener;
import pl.wat.nutpromobile.model.TrainingData;

public class MainActivity extends BaseActivity<MainActivityViewModel>
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

    @Inject
    MainActivityViewModel viewModel;

    @Override
    public MainActivityViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, TAG + " created");
        setContentView(R.layout.activity_main);
        permission.startPermissionRequest();
        ButterKnife.bind(this);
        setupNavigation();
        getLifecycle().addObserver(userLocation);
        getLifecycle().addObserver(bluetoothConnection);
        getLifecycle().addObserver(training);
        MyNotification.createNotificationChannel(this);
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
