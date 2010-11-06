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

package org.teleportr.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.teleportr.Teleporter;
import org.teleportr.model.Place;
import org.teleportr.model.Ride;
import org.teleportr.plugin.IPlugIn;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.SystemClock;
import android.util.Log;

public class QueryMultiplexer implements OnSharedPreferenceChangeListener {

    private static final String TAG = "Multiplexer";
    private Thread worker;
    public ArrayList<Ride> rides;
    public ArrayList<IPlugIn> plugIns;
    private ArrayList<Ride> nextRides;
    public HashMap<IPlugIn, Ride> latest;
	private Teleporter teleporter;
	private boolean noCar;
	private boolean car;
	private boolean taxi;


    public QueryMultiplexer(Teleporter tlp) {
        teleporter = tlp;

        
        nextRides = new ArrayList<Ride>();
        latest = new HashMap<IPlugIn, Ride>();
        
        rides = new ArrayList<Ride>() {
        	
            @Override
            public synchronized boolean add(Ride object) {
                if(!contains(object))
                    return super.add(object);
                else 
                    return false;
            }
            
            @Override
            public synchronized boolean addAll(Collection<? extends Ride> collection) {
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
//            public void onReceive(Context context, Intent uri) {
//                Log.d(TAG, "Plugin Response Received.");
//                final int duration = uri.getIntExtra("dur", -1);
//
//        this.ctx.registerReceiver(this.mPluginResponseReceiver, new IntentFilter("org.teleporter.intent.action.RECEIVE_RESPONSE"));
        
        SharedPreferences plugInSettings = teleporter.getSharedPreferences("plugIns", teleporter.MODE_PRIVATE);
        plugInSettings.registerOnSharedPreferenceChangeListener(this);
        onSharedPreferenceChanged(plugInSettings, "plugIns");
    }
    
    @Override
    public void onSharedPreferenceChanged(SharedPreferences plugInSettings, String key) {
        Log.d(TAG, "onSharedPreferenceChanged");
        
        plugIns = new ArrayList<IPlugIn>();
        try {
        	
        	if (plugInSettings.getBoolean("TaxiPlugIn", false)) {
        		taxi = true;
        		plugIns.add((IPlugIn) Class.forName("org.teleportr.plugin.CloudMadeDrivePlugIn").newInstance());
        		if (!plugInSettings.getBoolean("CloudMadeDrivePlugIn", false))
        			car = false;
        		else
        			car = true;
        			
        	}
        		
            for (String p : plugInSettings.getAll().keySet()) {
                if (plugInSettings.getBoolean(p, false)){
                    Log.d(TAG, " + plugin "+p+" selected");
                    if (!(taxi && p.equals("CloudMadeDrivePlugIn")))
                    	plugIns.add((IPlugIn) Class.forName("org.teleportr.plugin."+p).newInstance());
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
            	
            	int before = rides.size();
                for (IPlugIn plugin : plugIns) {
                    
                	Date time = null;
                	if (latest.containsKey(plugin)) {
                		time = latest.get(plugin).dep;
                		if (time == null)
                			continue; // this plugin won't find any later rides 
                		nextRides.addAll(plugin.find(orig, dest, time, teleporter));
                	} else
                		nextRides.addAll(plugin.find(orig, dest, new Date(), teleporter));
                		
                    if (!nextRides.isEmpty())
                    	latest.put(plugin, nextRides.get(nextRides.size()-1));

                    Log.d(TAG, plugin.getClass().getName()+" found: "+nextRides.size());
                    if (!(!car && plugin.getClass().getName().equals("org.teleportr.plugin.CloudMadeDrivePlugIn")))
                    	rides.addAll(nextRides);
                    nextRides.clear();
                	teleporter.getContentResolver().notifyChange(Ride.URI, null);    
                }
                
                if (before == rides.size())
                SystemClock.sleep(5000);
                
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
    	final Date threeMinutesAgo = new Date(System.currentTimeMillis()-180000);
    	final ArrayList<Ride> rides = this.rides;
		for(int i = rides.size() - 1; i >= 0; i--) {
			final Ride r = rides.get(i);
			if(r.dep != null && r.dep.before(threeMinutesAgo)) {
				rides.remove(i);
			}
		}
    }

    public void sort() {

//        priorities = (Map<String, Integer>) ctx.getSharedPreferences("priorities", ctx.MODE_PRIVATE).getAll();
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
