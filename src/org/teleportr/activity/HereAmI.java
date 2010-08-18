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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.teleportr.R;
import org.teleportr.Teleporter;
import org.teleportr.R.id;
import org.teleportr.R.layout;
import org.teleportr.model.Place;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchManager.OnCancelListener;
import android.app.SearchManager.OnDismissListener;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class HereAmI extends Activity {

    private static final String TAG = "HereAmI";
	private Teleporter teleporter;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.hereami);
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
         findViewById(R.id.nmb).setOnClickListener(new OnClickListener() {
        	 
        	 @Override
        	 public void onClick(View v) {
        		 v.setVisibility(View.GONE);
        		 EditText nmb = (EditText) findViewById(R.id.nmb_edit);
        		 nmb.setText(((TextView)v).getText());
        		 nmb.setVisibility(View.VISIBLE);
        		 nmb.requestFocus();
        		 nmb.selectAll();
        		 ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(nmb, 0);
        		 
        		 nmb.setOnEditorActionListener(new OnEditorActionListener() {
        			 
        			 @Override
        			 public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        				 v.setVisibility(View.GONE);
        				 Log.d(TAG, "action "+actionId);
        				 TextView nmb = (TextView) findViewById(R.id.nmb);
        				 nmb.setVisibility(View.VISIBLE);

        				 if (teleporter.currentPlace.address.matches("(.*)\\s+(\\d+)"))
        					 teleporter.currentPlace.address = teleporter.currentPlace.address.replace(nmb.getText(), v.getText());
        				 else
        					 teleporter.currentPlace.address += " "+v.getText();
        				 display(teleporter.currentPlace);
        				 return false;
        			 }
        		 });
        		 
        	 }
         });
         
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
         display(teleporter.currentPlace);
         onSearchRequested();
    }
    

	@Override
	public boolean onSearchRequested() {
		if (teleporter.currentPlace != null) {
			startSearch(teleporter.currentPlace.name, true, null, false);
			return true;
		}
		return super.onSearchRequested();
	}

	private void display(Place place) {
		if (place != null) {
			((TextView)findViewById(R.id.name)).setText(place.name);
			((TextView)findViewById(R.id.latlon)).setText(place.lat+"\n"+place.lon);
			((TextView)findViewById(R.id.city)).setText(place.city);
			((ImageView)findViewById(R.id.icon)).setImageResource(place.icon);
			if (place.address != null) {
				Matcher address = Pattern.compile("(.*)\\s+(\\d+)").matcher(place.address);
				if (address.find()) {
					((TextView)findViewById(R.id.street)).setText(address.group(1));
					((TextView)findViewById(R.id.nmb)).setText(address.group(2));
				} else {
					((TextView)findViewById(R.id.street)).setText(place.address);
					((TextView)findViewById(R.id.nmb)).setText("??");
				}
			}
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
        
        display(place);
		if (place != null && place.icon == R.drawable.a_street) {
			Toast.makeText(HereAmI.this, "house number and city ok?", Toast.LENGTH_LONG).show();
		} else finish(); 
    }


}
