package com.example.fileio;

import android.preference.PreferenceActivity;
import android.os.Bundle;

public class PrefsActivity extends PreferenceActivity{

    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prefs_activity);

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

    }
}
