package pl.wat.nutpromobile.fragments.settings;


import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import pl.wat.nutpromobile.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
