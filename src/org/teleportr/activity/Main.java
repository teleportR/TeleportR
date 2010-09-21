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
import org.teleportr.R.drawable;
import org.teleportr.R.id;
import org.teleportr.R.layout;
import org.teleportr.R.menu;
import org.teleportr.model.Place;
import org.teleportr.model.Ride;
import org.teleportr.util.LogCollector;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends ListActivity implements OnSeekBarChangeListener {
    
	private SharedPreferences priorities; // criteria..
    private BroadcastReceiver timetick; // every minute..
    private ContentObserver refresh; // new rides..
    private Teleporter teleporter; // to beam..
    private Ride[] rides; // results..

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        
        // accept EULA
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("eula_accepted", false)) {
        	new AlertDialog.Builder(this).setTitle("EULA").setMessage(getString(R.string.eula))
	        	.setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
	        		public void onClick(DialogInterface dialog, int whichButton) {
	        			PreferenceManager.getDefaultSharedPreferences(Main.this).edit().putBoolean("eula_accepted", true).commit();
	        			getSharedPreferences("plugIns", MODE_WORLD_WRITEABLE).edit().putBoolean("BahnDePlugIn", true).commit();
	        		}})
        		.setNegativeButton(getString(R.string.reject), new DialogInterface.OnClickListener() {
        			public void onClick(DialogInterface dialog, int whichButton) {
        				Toast.makeText(Main.this, "sorry!", Toast.LENGTH_SHORT);
        				finish();
        			}
        		}).create().show();
        }
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
        setContentView(R.layout.main);

        teleporter = (Teleporter) getApplication();
        
        // set origin place
        findViewById(R.id.orig).setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		startActivityForResult(new Intent(Main.this, HereAmI.class), 0);
        	}
        });

        //set destination place
        findViewById(R.id.dest).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchRequested();
            }
        });
        
        // tips by scotty
        findViewById(R.id.logo).setOnClickListener(new OnClickListener() {
        	private int tip = 0;

			@Override
        	public void onClick(View v) {
        		if (tip > 0 && getSharedPreferences("autocompletion", 0).getAll().isEmpty()) {
        			tip = 1;
        			openOptionsMenu();
        		}
        		startActivity(new Intent(getResources().getStringArray(R.array.tips)[tip++], null, Main.this, Help.class));
        	}
        });
        
        // search results
        if (state != null) {
        	teleporter.origin = state.getParcelable("orig");
        	teleporter.destination = state.getParcelable("dest");
        	Parcelable[] pars = state.getParcelableArray("rides");
        	rides = new Ride[pars.length];
        	for (int i = 0; i < pars.length; i++) rides[i] = (Ride) pars[i];
        } else
        	rides = teleporter.getRides(new Ride[0]);

        bindUI(); // origin/destination buttons
        bindSlidingDrawer(); // priorities pane

        setListAdapter(new BaseAdapter() {
            
            @Override
            public View getView(int position, View view, ViewGroup parent) {
                if (view == null) {
                	if (position < rides.length)
                		view = getLayoutInflater().inflate(R.layout.rideview, parent, false);
                	else { // show spinning progress loading
                		view = getLayoutInflater().inflate(R.layout.loading, parent, false);
                		final RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                		rotateAnimation.setDuration(600);
                		rotateAnimation.setRepeatMode(Animation.RESTART);
                		rotateAnimation.setRepeatCount(Animation.INFINITE);
                		view.findViewById(R.id.iv_loadingview_loading).startAnimation(rotateAnimation);
                	}
                }
                if (position < rides.length)
                    ((RideView)view).setRide(rides[position]);
                else 
                	teleporter.beam(); // search for more rides
                return view;
            }
            
            @Override
            public Object getItem(int position) {
            	return (position < rides.length)? rides[position]:null;
            }
            
            @Override
            public int getCount() { return rides.length+1; }
            
            @Override
            public int getViewTypeCount() { return 2; }
            
            @Override
            public int getItemViewType(int position) {
            	return (position < rides.length)? 0:1;
            }
            
            @Override
            public boolean hasStableIds() { return false; }

            @Override
            public long getItemId(int position) {
                return (position < rides.length)? rides[position].hashCode() : 2342;
            }
        });
        
        refresh = new ContentObserver(new Handler()) {
        	@Override
        	public void onChange(boolean selfChange) {
        		Log.d(Teleporter.TAG, "refresh rides list");
        		rides = teleporter.getRides(rides);
        		getListView().invalidateViews();
        	}
        };
        refresh.onChange(true);
        
        timetick = new BroadcastReceiver() {
        	@Override
        	public void onReceive(final Context pContext, final Intent pIntent) {
        		Log.d(Teleporter.TAG, "count down another minute");
        		refresh.onChange(true);
        	}
        };
    }
    
    private void bindUI() {
        if (teleporter.origin != null)
        	((TextView)findViewById(R.id.orig)).setText(teleporter.origin.name);
		if (teleporter.destination != null)
			((TextView)findViewById(R.id.dest)).setText(teleporter.destination.name);
		if (teleporter.origin != null && teleporter.destination != null) {
			findViewById(R.id.logo).setVisibility(View.GONE);
			getListView().setVisibility(View.VISIBLE);
		}
	}

	
	@Override
	protected void onStart() {
		super.onStart();
//		Log.d(Teleporter.TAG, "onStart");
		registerReceiver(timetick, new IntentFilter(Intent.ACTION_TIME_TICK));
		getContentResolver().registerContentObserver(Ride.URI, false, refresh);
	}

    @Override
    protected void onStop() {
    	super.onStop();
//    	Log.d(Teleporter.TAG, "onStop");
    	unregisterReceiver(this.timetick);
    	getContentResolver().unregisterContentObserver(refresh);
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

		bindUI();
		if (teleporter.origin != null)
			Log.d(Teleporter.TAG, "changed origin place: "+teleporter.origin.name);
	}

	@Override
    protected void onNewIntent(Intent intent) {
        
        Place destination = null;
        if (intent.getData() != null) {
        	destination = Place.find(intent.getData(), this);
        } else if (intent.hasExtra(SearchManager.QUERY)) {
        	destination = Place.find(intent.getStringExtra(SearchManager.QUERY), this);
        }
        
        if (destination != null && destination.name != null) {
        	if (teleporter.destination != null)
        		teleporter.reset();
        	teleporter.destination = destination;
            if (teleporter.origin != null)
            	teleporter.beam();       
            bindUI();
            Log.d(Teleporter.TAG, "changed destination place: "+teleporter.destination.name);
        }
    }
	
	

    @Override
	protected void onSaveInstanceState(Bundle state) {
    	super.onSaveInstanceState(state);
    	Log.d(Teleporter.TAG, "SAVE STATE");
    	state.putParcelableArray("rides", rides);
    	state.putParcelable("orig", teleporter.origin);
    	state.putParcelable("dest", teleporter.destination);
	}

	@Override
    protected void onListItemClick(final ListView pListView, final View pView, final int pPosition, final long pID) {
    	Log.d(Teleporter.TAG, "clicked on search result ride: ");
        Ride ride = (Ride) getListAdapter().getItem(pPosition);
        if (ride != null && ride.intent != null)
        	startActivity(ride.intent);
        else
        	startActivity(new Intent(getString(R.string.explain_mock_plugin), null, Main.this, Help.class));
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        case R.id.about:
        	startActivity(new Intent(this, About.class));
            break;

        case R.id.settings:
            startActivity(new Intent(this, Settings.class));
            break;
            
        case R.id.feedback:
        	new AlertDialog.Builder(this)
	            .setTitle("feedback")
	            .setIcon(android.R.drawable.ic_dialog_info)
	            .setPositiveButton("send logs", new DialogInterface.OnClickListener(){
	                public void onClick(DialogInterface dialog, int whichButton){
	                	LogCollector.feedback(Main.this, "scotty@teleportr.org, flo@andlabs.de");
	                }
	            })
	            .setNeutralButton("mail scotty", new Dialog.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:scotty@teleportr.org, flo@andlabs.de"));
	                    intent.putExtra(Intent.EXTRA_SUBJECT, "feedback "+getString(R.string.app_name));
						try {
							PackageInfo info = getPackageManager().getPackageInfo("org.teleportr", PackageManager.GET_META_DATA);
							intent.putExtra(Intent.EXTRA_TEXT, "version: "+info.versionName+" ("+info.versionCode+") \n");
						} catch (NameNotFoundException e) {}
	                    startActivity(intent); 
	                }}
	            )
            .show();
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    
/** search priorities **/
    
    private SeekBar fun;
    private SeekBar eco;
    private SeekBar fast;
    private SeekBar green;
    private SeekBar social;

    private void bindSlidingDrawer() {

    	SlidingDrawer slider = (SlidingDrawer) findViewById(R.id.priorities);
    	slider.setOnDrawerOpenListener(new OnDrawerOpenListener() {

    		@Override
    		public void onDrawerOpened() {
    			((ImageView)findViewById(R.id.handle)).setImageResource(R.drawable.handle_close);

    		}
    	});
    	slider.setOnDrawerCloseListener(new OnDrawerCloseListener() {

    		@Override
    		public void onDrawerClosed() {
    			((ImageView)findViewById(R.id.handle)).setImageResource(R.drawable.handle);

    		}
    	});

    	priorities = getSharedPreferences("priorities", MODE_PRIVATE);
    	fun = ((SeekBar)findViewById(R.id.fun));
    	eco = ((SeekBar)findViewById(R.id.eco));
    	fast = ((SeekBar)findViewById(R.id.fast));
    	green = ((SeekBar)findViewById(R.id.green));
    	social = ((SeekBar)findViewById(R.id.social));
    	fun.setOnSeekBarChangeListener(this);
    	eco.setOnSeekBarChangeListener(this);
    	fast.setOnSeekBarChangeListener(this);
    	green.setOnSeekBarChangeListener(this);
    	social.setOnSeekBarChangeListener(this);

    	fun.setProgress(priorities.getInt("fun", 0));
    	eco.setProgress(priorities.getInt("eco", 0));
    	fast.setProgress(priorities.getInt("fast", 0));
    	green.setProgress(priorities.getInt("green", 0));
    	social.setProgress(priorities.getInt("social", 0));
    }
    

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    	Log.d(Teleporter.TAG, "changed search priority");
    	priorities.edit().putInt((String)seekBar.getTag(), progress).commit();
//        multiplexer.sort();
    	getListView().invalidateViews();
    }
    
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
        
    }

	@Override
	public void onBackPressed() {
		SlidingDrawer slider = (SlidingDrawer) findViewById(R.id.priorities);
		if (slider.isOpened())
			slider.close();
		else
			super.onBackPressed();
	}
    
    @Override
	public boolean onSearchRequested() {
		if (teleporter.destination != null) {
			startSearch(teleporter.destination.name, true, null, false);
			return true;
		}
		return super.onSearchRequested();
	}

}
