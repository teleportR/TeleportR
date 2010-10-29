package org.teleportr.plugin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.teleportr.R;
import org.teleportr.Teleporter;
import org.teleportr.model.Place;
import org.teleportr.model.Ride;

import android.app.SearchManager;
import android.util.Log;

public class CloudMadePlugIn implements IPlugIn {
	//not needed 
	private DefaultHttpClient client;
	private ArrayList<Ride> rides;
	private static final String TAG = "CloudeMadePlugIn";
	
	// needed
	// api key
//	private String CloudemadeApiKey = "YOURCLOUDMADEAPIKEY";
	final static String CloudemadeApiKey = "07617baa158745f49d5f1c86d4f83fdb";
	final static String[] TravelModes = new String[]{"bicycle","car"};
	
	
	//not needed 	
	public CloudMadePlugIn() {
	        rides = new ArrayList<Ride>();
	        client = new DefaultHttpClient();
	}
	
    @Override
    public List<Ride> find(Place o, Place d, Date timeh) {
        
        StringBuilder url = new StringBuilder();
        url.append("http://routes.cloudmade.com/"); 
        url.append(CloudemadeApiKey);
        url.append("/api/0.3/");
        url.append(o.lat/1E6).append(","); 
        url.append(o.lon/1E6).append(","); 
        url.append(d.lat/1E6).append(","); 
        url.append(d.lon/1E6).append("/"); 
        url.append(TravelModes[0]);
        url.append(".js");
        
        Log.d(TAG, "url: "+url.toString());
        
        try {
            Ride r;
            MatchResult m;
            HttpGet get = new HttpGet(url.toString());
            HttpResponse response = client.execute(get);
            Scanner scanner = new Scanner(response.getEntity().getContent());
            while (scanner.findWithinHorizon("distance\":(\\d+),.*time\":(\\d+),", 10000) != null) {
                m = scanner.match();
                Log.d(TAG,"distance: "+m.group(1)+"m");
                
                r = new Ride();
                r.orig = o;
                r.dest = d;
                r.duration = Integer.valueOf(m.group(2))/60;
                r.mode = Ride.MODE_BIKE;
                r.uri = url.toString();
                
                r.price = -1;
                r.fun = 3;
                r.eco = 3;
                r.fast = 1;
                r.social = 2;
                r.green = 4;
                
                rides.add(r);
            }
        
        } catch (Exception e) {
            Log.e(TAG, "Mist!");
            e.printStackTrace();
        }
        return rides;      
    }

	@Override
	public void share(Ride offer) {
		// TODO Auto-generated method stub
	}

}
