/**
 * Copyright (c) 2010: andlabs gbr, teleportr.org All rights reserved.
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
import org.teleportr.Teleporter;
import org.teleportr.R.id;
import org.teleportr.R.layout;
import org.teleportr.model.Place;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class HereAmI extends Activity {

    private static final String TAG = "HereAmI";
	private Teleporter teleporter;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.place_detail);
        super.onCreate(savedInstanceState);
        
         findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			
			    @Override
			    public void onClick(View v) {
				    finish();
			      }
		    });
         findViewById(R.id.name).setOnClickListener(new OnClickListener() {
			
			    @Override
			    public void onClick(View v) {
				    onSearchRequested();
			      }
		    });
         
         teleporter = (Teleporter) getApplication();
         display(teleporter.currentPlace);
         
         onSearchRequested();
    }

	private void display(Place place) {
		if (place != null) {
			((TextView)findViewById(R.id.name)).setText(place.name);
			((TextView)findViewById(R.id.latlon)).setText(place.lat+"\n"+place.lon);
			((TextView)findViewById(R.id.address)).setText(place.address+", "+place.city);
			((ImageView)findViewById(R.id.icon)).setImageResource(place.icon);
		}
	}
    
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "newIntent: "+intent.toString());
        
        Place place = null;
        if (intent.getData() != null) {
        	place = Place.find(intent.getData(), this);
        } else if (intent.hasExtra(SearchManager.QUERY)) {
        	place = Place.find(intent.getStringExtra(SearchManager.QUERY), this);
        }
        if (teleporter.currentPlace != null) 
        	teleporter.reset();
        
        teleporter.currentPlace = place;
        teleporter.beam();
        
        if (place.name!=null) // unambigious
        	finish(); // back to search results

        display(place);
    }

	@Override
	public boolean onSearchRequested() {
		if (teleporter.currentPlace != null) {
			startSearch(teleporter.currentPlace.name, true, null, false);
			return true;
		}
		return super.onSearchRequested();
	}
    
}
