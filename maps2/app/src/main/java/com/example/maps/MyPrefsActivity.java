package com.example.maps;

import android.preference.PreferenceActivity;
import android.os.Bundle;

public class MyPrefsActivity extends PreferenceActivity
{

    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prefs_activity);

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

    }

}

