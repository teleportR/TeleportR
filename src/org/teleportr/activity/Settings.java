package org.teleportr.activity;

import org.teleportr.R;
import org.teleportr.R.xml;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class Settings extends PreferenceActivity {

    private static final String TAG = "Settings";
    private SharedPreferences prefs;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        
        getPreferenceManager().setSharedPreferencesName("plugIns");
        
        addPreferencesFromResource(R.xml.settings);

        ((PreferenceScreen)findPreference("autocompletion"))
        .setIntent(new Intent(this, Autocompletion.class));
    }


   

}
