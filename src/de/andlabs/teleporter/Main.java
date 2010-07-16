package de.andlabs.teleporter;

import de.andlabs.teleporter.plugin.ITeleporterPlugIn;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
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

public class Main extends ListActivity implements OnSeekBarChangeListener {
    
    private BroadcastReceiver timetick; // every minute..
    private ContentObserver observer; // new rides..
    private SharedPreferences priorities; // criteria..
    private Teleporter teleporter; // to beam..
    private Ride[] rides; // results..

    private SeekBar fun;
    private SeekBar eco;
    private SeekBar fast;
    private SeekBar green;
    private SeekBar social;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (getSharedPreferences("autocompletion", MODE_PRIVATE).getAll().isEmpty()) {
            getSharedPreferences("plugIns", MODE_WORLD_WRITEABLE).edit().putBoolean("BahnDePlugIn", true).commit();
            startActivity(new Intent(this, DownloadsActivity.class));
        }

        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.rides);
        
        findViewById(R.id.orig).setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		startActivityForResult(new Intent(Main.this, HereIAmActivity.class), 0);
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
        findViewById(R.id.logo).setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		onSearchRequested();
        	}
        });

        // this is it
        teleporter = (Teleporter) getApplication();
        
        if (teleporter.currentPlace != null)
        	((TextView)findViewById(R.id.orig)).setText(teleporter.currentPlace.name);
		if (teleporter.destination != null)
			((TextView)findViewById(R.id.dest)).setText(teleporter.destination.name);
        

		// search results
		rides = teleporter.getRides(new Ride[0]);
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
        
        observer = new ContentObserver(new Handler()) {
        	
        	@Override
        	public void onChange(boolean selfChange) {
        		Log.d(Teleporter.TAG, "new rides found");
        		rides = teleporter.getRides(rides);
        		if (rides.length > 0) {
        			findViewById(R.id.logo).setVisibility(View.GONE);
        			getListView().setVisibility(View.VISIBLE);
        		}
        		else {
        			findViewById(R.id.logo).setVisibility(View.VISIBLE);
        			getListView().setVisibility(View.GONE);
        		}
        		onContentChanged();
        	}
        };
        observer.onChange(true);
        
        timetick = new BroadcastReceiver() {
        	@Override
        	public void onReceive(final Context pContext, final Intent pIntent) {
        		Log.d(Teleporter.TAG, "count down another minute");
        		getListView().invalidateViews();
        	}
        };

        SlidingDrawer slider = ((SlidingDrawer)findViewById(R.id.priorities));
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
//        multiplexer.sort();
        
        
    }
    
	@Override
	protected void onStart() {
		super.onStart();
		Log.d(Teleporter.TAG, "onStart");
		
		registerReceiver(timetick, new IntentFilter(Intent.ACTION_TIME_TICK));
		getContentResolver().registerContentObserver(Ride.URI, false, observer);
	}
	
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d(Teleporter.TAG, "onResume");
//    }
//
//    @Override
//    protected void onPause() {
//    	Log.d(Teleporter.TAG, "onPause");
//        super.onPause();
//    }

    @Override
    protected void onStop() {
    	super.onStop();
    	Log.d(Teleporter.TAG, "onStop");
    	unregisterReceiver(this.timetick);
    	getContentResolver().unregisterContentObserver(observer);
    }
    
    
    
    
/** user interaction **/
    
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Log.d(Teleporter.TAG, "changed current place: ");
		if (teleporter.currentPlace != null)
			((TextView)findViewById(R.id.orig)).setText(teleporter.currentPlace.name);
	}

	@Override
    protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
        Log.d(Teleporter.TAG, "changed destination place: ");
        
        Place destination = null;
        if (intent.getData() != null) {
        	destination = Place.find(intent.getData(), this);
        } else if (intent.hasExtra(SearchManager.QUERY)) {
        	destination = Place.find(intent.getStringExtra(SearchManager.QUERY), this);
        }
        if (teleporter.destination != null)
        	teleporter.reset();
        
        // GO..
        teleporter.destination = destination;
        teleporter.beam();
        
//        MediaPlayer.create(this, R.raw.sound_long).start();
        if (destination.name != null)
        	((TextView)findViewById(R.id.dest)).setText(destination.name);
        else
        	((TextView)findViewById(R.id.dest)).setText(destination.address);
        	
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    	Log.d(Teleporter.TAG, "changed search priorities: ");
    	priorities.edit().putInt((String)seekBar.getTag(), progress).commit();
//        multiplexer.sort();
    	getListView().invalidateViews();
    }

    @Override
    protected void onListItemClick(final ListView pListView, final View pView, final int pPosition, final long pID) {
    	Log.d(Teleporter.TAG, "clicked on search result ride: ");
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

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
        
    }

}