package com.example.yardscapelistingprototype.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.yardscapelistingprototype.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}