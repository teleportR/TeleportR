package de.andlabs.teleporter;

import java.net.URLDecoder;
import java.util.Map.Entry;

import de.andlabs.teleporter.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Main extends ListActivity implements OnSeekBarChangeListener {
    
    private static final String TAG = "Teleporter";
	protected static final int ORIG = 0;
    private BroadcastReceiver mTimeTickReceiver;
    private QueryMultiplexer multiplexer;
    private SeekBar fun;
    private SeekBar eco;
    private SeekBar fast;
    private SeekBar green;
    private SeekBar social;
    private SharedPreferences priorities;
	private Teleporter teleporter;
	private Ride[] rides;
	private ContentObserver mContentObserver;
	private View progress;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (getSharedPreferences("autocompletion", MODE_PRIVATE).getAll().isEmpty()) {
            getSharedPreferences("plugIns", MODE_WORLD_WRITEABLE).edit().putBoolean("BahnDePlugIn", true).commit();
            startActivity(new Intent(this, DownloadsActivity.class));
        }

        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        rides = new Ride[0];
        setContentView(R.layout.rides);
        teleporter = (Teleporter) getApplication();

        setListAdapter(new BaseAdapter() {
            
            @Override
            public View getView(int position, View view, ViewGroup parent) {
                if (view == null) {
                	if (position < rides.length)
                		view = getLayoutInflater().inflate(R.layout.rideview, parent, false);
                	else {
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
        
        mContentObserver = new ContentObserver(new Handler()) {

			@Override
			public void onChange(boolean selfChange) {
				Log.d(TAG, "content changed ");
				rides = teleporter.getRides();
				getListView().invalidateViews();
			}
		};
		
		mTimeTickReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(final Context pContext, final Intent pIntent) {
				getListView().invalidateViews();
			}
		};
        
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
//        multiplexer.sort();
        
        
        findViewById(R.id.orig).setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		startActivityForResult(new Intent(Main.this, HereIAmActivity.class), ORIG);
        	}
        });
        findViewById(R.id.arrow).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchRequested();
            }
        });
        findViewById(R.id.dest).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchRequested();
            }
        });
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d("aha", "changed "+progress);
        priorities.edit().putInt((String)seekBar.getTag(), progress).commit();
        multiplexer.sort();
        getListView().invalidateViews();
    }
    
    
	@Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "newIntent: "+intent.toString());
//        MediaPlayer.create(this, R.raw.sound_long).start();
        
        String query;
        Place destination = null;

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

        	getListView().setVisibility(View.VISIBLE);
        	
        	destination = Place.find(intent.getData(), this);
        	teleporter.destination = destination;
        	teleporter.beam();
            
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            query = URLDecoder.decode(getIntent().getDataString().substring(10));
            String[] q = query.split(",");
            if (Character.isDigit(query.charAt(0))) {
                query = q[0].substring(q[0].indexOf(" ")+1) +" "+ q[0].substring(0, q[0].indexOf(" "))+", "+q[1].split(" ")[1];
            } else {
                query = q[0]+", "+ q[1].split(" ")[2];
            }
        }
        if (destination != null) {
            
            ((TextView)findViewById(R.id.dest)).setText(destination.name);
        }
        super.onNewIntent(intent);
    }


	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
		((TextView)findViewById(R.id.orig)).setText(((Teleporter)getApplication()).currentPlace.name);
		getContentResolver().registerContentObserver(Ride.URI, false, mContentObserver);
		registerReceiver(mTimeTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
	}
	
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
    	Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
    	super.onStop();
    	Log.d(TAG, "onStop");
    	getContentResolver().unregisterContentObserver(mContentObserver);
    	unregisterReceiver(this.mTimeTickReceiver);
    }

    @Override
    protected void onListItemClick(final ListView pListView, final View pView, final int pPosition, final long pID) {
        Ride ride = (Ride) getListAdapter().getItem(pPosition);
        startActivity(ride.intent);
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

        case R.id.search:
            onSearchRequested();
            break;

        case R.id.settings:
            startActivity(new Intent(this, SettingsActivity.class));
            break;
        case R.id.feedback:
            LogCollector.feedback(this, "flo@andlabs.de", "bla bla");
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void datasetChanged() {
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

}