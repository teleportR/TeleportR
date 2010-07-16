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
        super.onCreate(savedInstanceState);
        
        onSearchRequested();

        setContentView(R.layout.place_detail);
        teleporter = (Teleporter) getApplication();
        display(teleporter.currentPlace);
    }

	private void display(Place place) {
		((TextView)findViewById(R.id.name)).setText(place.name);
		((TextView)findViewById(R.id.latlon)).setText(place.lat+"\n"+place.lon);
		((TextView)findViewById(R.id.address)).setText(place.address+", "+place.city);
		((ImageView)findViewById(R.id.icon)).setImageResource(place.icon);
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
	protected void onStart() {
		Log.d(TAG, "onStart");
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}
    
}
