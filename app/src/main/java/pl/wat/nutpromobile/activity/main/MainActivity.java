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

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.wat.nutpromobile.R;
import pl.wat.nutpromobile.features.ble.Connection;
import pl.wat.nutpromobile.features.location.UserLocation;
import pl.wat.nutpromobile.features.training.Training;
import pl.wat.nutpromobile.features.training.TrainingListener;
import pl.wat.nutpromobile.fragments.connection.OnConnectionFragmentInteractionListener;
import pl.wat.nutpromobile.fragments.training.OnTrainingFragmentInteractionListener;
import pl.wat.nutpromobile.model.TrainingData;
import pl.wat.nutpromobile.util.NotificationCreator;

public class MainActivity extends AppCompatActivity implements OnConnectionFragmentInteractionListener,
        OnTrainingFragmentInteractionListener, SharedPreferences.OnSharedPreferenceChangeListener, TrainingListener {
    public final static String TAG = "Custom: " + MainActivity.class.getSimpleName();
    public static final String INTENT_FIRST_LAUNCH = "pl.wat.nutpromobile.MainActivity.FIRST_LAUNCH";

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private Connection connection;
    private UserLocation userLocation;
    private Permission permission;
    private PreferencesManager preferencesManager;

    private Training training;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, TAG + " created");
        setContentView(R.layout.activity_main);
        permission = new Permission(this);
        permission.startPermissionRequest();
        ButterKnife.bind(this);
        setupNavigation();
        connection = new Connection(this, getLifecycle());
        userLocation = new UserLocation(this);
        training = new Training(this, connection, userLocation);
        getLifecycle().addObserver(connection);
        preferencesManager = new PreferencesManager(this);
        NotificationCreator.createNotificationChannel(this);
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
    public Connection getConnection() {
        return connection;
    }


    private void setupNavigation() {
        NavigationUI.setupWithNavController(navigation, Navigation.findNavController(this, R.id.nav_host_fragment));
        checkIfIsFirstLaunchAndNavigate();
    }

    private void checkIfIsFirstLaunchAndNavigate() {
        Intent intent = getIntent();
        Boolean firstLunch = intent.getBooleanExtra(MainActivity.INTENT_FIRST_LAUNCH, true);
        if (firstLunch) {
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.connection);
        }
    }

    @Override
    public void startTraining() {
        System.out.println("Activity START Click");
        training.startTraining();
    }

    @Override
    public void stopTraining() {
        System.out.println("Activity STOP Click");
        initStopTraining();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, TAG + " destroyed");
        initStopTraining();
        super.onDestroy();
    }

    private void initStopTraining() {
        if (training != null) {
            training.removeTrainingListener();
            training.stopTraining();
        }
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
    public void onTrainingDataProcessed(TrainingData trainingData) {
        System.out.println(trainingData.getSensoricData().getRawData());
    }
}
