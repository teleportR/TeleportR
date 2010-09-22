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

import org.teleportr.Teleporter;
import org.teleportr.model.Place;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchManager.OnCancelListener;
import android.app.SearchManager.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class HereAmI extends Activity {

    private static final String TAG = "HereAmI";
	private Teleporter teleporter;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    
    	super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
         
         ((SearchManager)getSystemService(SEARCH_SERVICE)).setOnCancelListener(new OnCancelListener() {
        	 @Override
        	 public void onCancel() {
        		 Log.d(TAG, "onSearchCanceled");
        		 finish(); 
        	 }
         }); 
         
         ((SearchManager)getSystemService(SEARCH_SERVICE)).setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				Log.d(TAG, "onSearchDismissed");
			}
		 });

         teleporter = (Teleporter) getApplication();
         onSearchRequested();
    }

    
	@Override
	public boolean onSearchRequested() {
		if (teleporter.origin != null) {
//			String text;
//			if (teleporter.origin.address != null)
//				text = teleporter.origin.address+", "+teleporter.origin.city;
//			else
//				text = teleporter.origin.name+", "+teleporter.origin.city;
			startSearch(teleporter.origin.name+", "+teleporter.origin.city, true, null, false);
			return true;
		}
		return super.onSearchRequested();
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
        
        teleporter.setOrigin(place);
        teleporter.beam();
        finish(); 
    }


}
