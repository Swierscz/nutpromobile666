package pl.wat.nutpromobile.activity.main;

import android.os.Bundle;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.wat.nutpromobile.R;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private MainActivityLifecycleObserver mainActivityLifecycleObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLifecycle().addObserver(new MainActivityLifecycleObserver(getLifecycle()));

        ButterKnife.bind(this);

        //NavigationUI.setupWithNavController(navigation, Navigation.findNavController(this, R.id.nav_host_fragment));
        NavigationUI.setupWithNavController(navigation, Navigation.findNavController(this, R.id.nav_host_fragment));
    }
}
