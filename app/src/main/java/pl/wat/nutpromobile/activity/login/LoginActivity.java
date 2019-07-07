package pl.wat.nutpromobile.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.wat.nutpromobile.R;
import pl.wat.nutpromobile.activity.main.MainActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (!isFirstLaunch())
            goToMainActivity(false);
    }

    @OnClick(R.id.loginButton)
    public void onLoginButtonClick(View view) {
        setFirstLaunchAchieved();
        goToMainActivity(true);
    }

    public void goToMainActivity(boolean firstLaunch) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.INTENT_FIRST_LAUNCH, firstLaunch);
        startActivity(intent);
    }

    public boolean isFirstLaunch() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getBoolean(getString(R.string.preference_is_first_launch), true);
    }

    public void setFirstLaunchAchieved() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.preference_is_first_launch), false);
        editor.commit();
    }
}
