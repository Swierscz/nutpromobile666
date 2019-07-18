package pl.wat.nutpromobile.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.wat.nutpromobile.NutproMobileApp;
import pl.wat.nutpromobile.R;
import pl.wat.nutpromobile.activity.main.MainActivity;
import pl.wat.nutpromobile.db.repository.UserRepository;
import pl.wat.nutpromobile.db.row.UserRow;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.firstNameEditText)
    TextInputEditText firstNameEditText;

    @BindView(R.id.lastNameEditText)
    TextInputEditText lastNameEditText;

    @BindView(R.id.ageEditText)
    TextInputEditText ageEditText;

    @BindView(R.id.heightEditText)
    TextInputEditText heightEditText;

    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (!isFirstLaunch())
            goToMainActivity(false);
        userRepository = ((NutproMobileApp)getApplication()).repositoryProvider
                .getUserRepositoryInstance((NutproMobileApp)getApplication());
    }

    @OnClick(R.id.loginButton)
    public void onLoginButtonClick(View view) {
        saveUserData();
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

    public void saveUserData() {
        UserRow userRow = new UserRow();
        String firstName = firstNameEditText.getText().toString();
        userRow.setFirstName(firstName);
        String lastName = lastNameEditText.getText().toString();
        userRow.setLastName(lastName);
        String ageString = ageEditText.getText().toString();
        int age;
        if (!ageString.equals("")) {
            age = Integer.parseInt(ageString);
        } else {
            age = 0;
        }
        userRow.setAge(age);
        String heightString = heightEditText.getText().toString();
        int height;
        if (!heightString.equals("")) {
            height = Integer.parseInt(heightString);
        } else {
            height = 0;
        }
        userRow.setHeight(height);
        userRepository.insertUser(userRow);
    }
}
