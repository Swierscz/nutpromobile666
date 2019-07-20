package pl.wat.nutpromobile.activity.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import javax.inject.Inject;

class PreferencesManager {
    private final static String TAG = PreferencesManager.class.getSimpleName();
    private MainActivity mainActivity;

    @Inject
    PreferencesManager(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        startPrefManage();
    }


    private void startPrefManage(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mainActivity.getApplicationContext());
        initialization(sharedPreferences);
    }

    private void initialization(SharedPreferences sharedPreferences){
        boolean isDarkMode =
                sharedPreferences.getBoolean("dark_mode_key", false);
        changeAppThemeToDarkMode(isDarkMode);
    }

    void setChangePreferencesBehaviours(SharedPreferences sharedPreferences, String key){
        if(key.equals("dark_mode_key")){
            Log.i(TAG, "Change dark mode theme triggered by user");
            boolean isDarkMode = sharedPreferences.getBoolean("dark_mode_key", true);
            changeAppThemeToDarkMode(isDarkMode);
        }
    }

    private void changeAppThemeToDarkMode(boolean isDarkMode){
        if(isDarkMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }




























}
