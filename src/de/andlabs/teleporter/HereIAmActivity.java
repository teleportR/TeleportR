package de.andlabs.teleporter;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Window;

public class HereIAmActivity extends Activity {

    private static final String TAG = "HereIAm";

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        onSearchRequested();
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "newIntent: "+intent.toString());
        Log.d(TAG, "newIntent: "+intent.getDataString());
        
        if (intent.getData() != null) {
        	Place p = Place.find(intent.getData(), this);
        	((Teleporter)getApplication()).currentPlace = p;
        	((Teleporter)getApplication()).beam();
        }
        finish();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
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
