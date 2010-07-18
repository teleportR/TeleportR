package org.teleportr.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.teleportr.model.Place;
import org.teleportr.model.Ride;
import org.teleportr.plugin.ITeleporterPlugIn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Handler;
import android.util.Log;

public class QueryMultiplexer implements OnSharedPreferenceChangeListener {

    private static final String TAG = "Multiplexer";
    public ArrayList<Ride> rides;
    public ArrayList<ITeleporterPlugIn> plugIns;
    private ArrayList<Ride> nextRides;
    private Map<String, Integer> priorities;
    public Ride latest;
    private BroadcastReceiver mPluginResponseReceiver;
    private final Context ctx;
    private Handler mUpdateHandler;
    private Thread worker;

    public QueryMultiplexer(Context ctx) {
        this.ctx = ctx;

        
        nextRides = new ArrayList<Ride>();
        rides = new ArrayList<Ride>() {
            @Override
            public boolean add(Ride object) {
                if(!contains(object))
                    return super.add(object);
                else 
                    return false;
            }
            
            @Override
            public boolean addAll(Collection<? extends Ride> collection) {
                for(Ride r : collection)
                    if(!contains(r))
                        super.add(r);
                return true;
            }
            
            @Override
            public void add(int index, Ride object) {
                if(!contains(object))
                    super.add(index, object);
            }
        };

//        this.mPluginResponseReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Log.d(TAG, "Plugin Response Received.");
//                final int duration = intent.getIntExtra("dur", -1);
//
//        this.ctx.registerReceiver(this.mPluginResponseReceiver, new IntentFilter("org.teleporter.intent.action.RECEIVE_RESPONSE"));
        
        SharedPreferences plugInSettings = ctx.getSharedPreferences("plugIns", ctx.MODE_PRIVATE);
        plugInSettings.registerOnSharedPreferenceChangeListener(this);
        onSharedPreferenceChanged(plugInSettings, "plugIns");
    }
    
    @Override
    public void onSharedPreferenceChanged(SharedPreferences plugInSettings, String key) {
        Log.d(TAG, "onSharedPreferenceChanged");
        
        plugIns = new ArrayList<ITeleporterPlugIn>();
        try {
            for (String p : plugInSettings.getAll().keySet()) {
                if (plugInSettings.getBoolean(p, false)){
                    Log.d(TAG, " + plugin "+p+" selected");
                    plugIns.add((ITeleporterPlugIn) Class.forName("org.teleportr.plugin."+p).newInstance());
                } else Log.d(TAG, " + plugin "+p+" NOT selected");
            }
        } catch (Exception e) {
            Log.e(TAG, "Schade!");
            e.printStackTrace();
        }

    }

    public boolean search(final Place orig, final Place dest) {
        // TODO use ThreadPoolExecutor ...

        if (worker != null && worker.isAlive())
            return false;
            
        worker = new Thread(new Runnable() {
            
            @Override
            public void run() {
            	
                for (ITeleporterPlugIn p : plugIns) {
                    
                    nextRides.addAll(p.find(orig, dest, (latest != null)? 
                    						latest.dep : new Date()));
                    
                    if (!nextRides.isEmpty())
                    	latest = nextRides.get(nextRides.size()-1);

                    Log.d(TAG, " + "+p+" found: "+nextRides.size());
                    rides.addAll(nextRides);
                    nextRides.clear();
                    
                }
                ctx.getContentResolver().notifyChange(Ride.URI, null);
                
//                Intent requestIntent = new Intent("org.teleporter.intent.action.RECEIVE_REQUEST");
//                requestIntent.putExtra("origLatitude", orig.lat);
//                requestIntent.putExtra("origLongitude", orig.lon);
//                requestIntent.putExtra("destLatitude", dest.lat);
//                requestIntent.putExtra("destLongitude", dest.lon);
//                ctx.sendBroadcast(requestIntent);
            }
        });
        worker.start();
        return true;
    }
    
    public void removeOutdated() { 
    	while (!rides.isEmpty() && rides.get(0).dep.before(new Date(System.currentTimeMillis()-180000)))
    		rides.remove(0); // if departured more than 3 minutes ago
    }

    public void sort() {

        priorities = (Map<String, Integer>) ctx.getSharedPreferences("priorities", ctx.MODE_PRIVATE).getAll();
//        Collections.sort(rides, new Comparator<Ride>() {
//
//            @Override
//            public int compare(Ride r1, Ride r2) {
//                // TODO Neue Faktor für Score: "Quickness" (Abfahrtszeit minus Jetzt)
//                // TODO Faktoren normalisieren
//                int score1= r1.fun * priorities.get("fun") +
//                r1.eco * priorities.get("eco") +
//                r1.fast * priorities.get("fast") +
//                r1.green * priorities.get("green") +
//                r1.social * priorities.get("social");
//                int score2= r2.fun * priorities.get("fun") +
//                r2.eco * priorities.get("eco") +
//                r2.fast * priorities.get("fast") +
//                r2.green * priorities.get("green") +
//                r2.social * priorities.get("social");
//                //                Log.d("aha", "score1: "+score1 + ",  score2: "+score2);
//                if (score1 < score2)
//                    return 1;
//                else if (score1 > score2)
//                    return -1;
//                else {
//                    if (r1.dep.after(r2.dep))
//                        return 1;
//                    if (r1.dep.before(r2.dep))
//                        return -1;
//                    return 0;
//                }
//            }
//        });
    }
}
