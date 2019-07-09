package pl.wat.nutpromobile.activity.splash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import pl.wat.nutpromobile.R;
import pl.wat.nutpromobile.activity.login.LoginActivity;
import pl.wat.nutpromobile.activity.main.MainActivity;

public class SplashActivity extends AppCompatActivity {
    public final static String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
