/**
 * Copyright (c) 2010: <http://www.teleportr.org/> All rights reserved.
 *	
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version <http://www.gnu.org/licenses/>
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
**/

package org.teleportr.activity;

import org.teleportr.R;
import org.teleportr.R.xml;
import org.teleportr.util.LogCollector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            
        case R.id.about:
        	startActivity(new Intent(this, About.class));
        	break;
            
        case R.id.feedback:
            LogCollector.feedback(this, "scotty@teleportr.org", "v1");
            break;
        }
        return super.onOptionsItemSelected(item);
    }
   

}
