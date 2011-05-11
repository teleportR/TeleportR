package org.teleportr.plugin;

import java.net.URLEncoder;
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

import android.util.Log;

public class GMapsDrivePlugIn implements IPlugIn {
	//not needed 
	private DefaultHttpClient client;
	private ArrayList<Ride> rides;
	private static final String TAG = "GMapsDrivePlugIn";
	
	
	public GMapsDrivePlugIn() {
	        rides = new ArrayList<Ride>();
	        client = new DefaultHttpClient();
	}
	
    @Override
    public List<Ride> find(Place orig, Place dest, Date time, Teleporter tlp) {

    	rides.clear();
    	
        StringBuffer url = new StringBuffer();
        url.append("http://maps.google.com/maps?f=d&hl=en");
        
        url.append("&saddr=");
        if (orig.address != null)
        	url.append(URLEncoder.encode(orig.address+(orig.city!=null? (", "+orig.city) : "")));
        else
        	url.append(URLEncoder.encode(orig.name+(orig.city!=null? (", "+orig.city) : "")+"!"));
		 
        url.append("&daddr=");// to
        if (dest.address != null)
        	url.append(URLEncoder.encode(dest.address+(dest.city!=null? (", "+dest.city) : "")+"!"));
        else
        	url.append(URLEncoder.encode(dest.name+(dest.city!=null? (", "+dest.city) : "")+"!"));

        url.append("&ie=UTF8&0&om=0&output=kml");

        
        
        Log.d(Teleporter.TAG, "url: "+url.toString());
        
        try {
            Ride r;
            MatchResult m;
            HttpGet get = new HttpGet(url.toString());
            HttpResponse response = client.execute(get);
            Scanner scanner = new Scanner(response.getEntity().getContent());

            while (scanner.findWithinHorizon("Distance: (.*)&.*\\(about (\\d*) hours? (\\d*) mins?|Distance: (.*)&.* (\\d*) mins?\\)", 100000) != null) {
                m = scanner.match();
                r = new Ride();
                
                if (m.group(1) != null) {
                	r.distance = (int) (Float.parseFloat(m.group(1))*1000);
                	r.duration += Integer.parseInt(m.group(2))*60;
                	r.duration += Integer.parseInt(m.group(3));
                } else {
                	r.distance = (int) (Float.parseFloat(m.group(4))*1000);
                	r.duration += Integer.parseInt(m.group(5));
                }
                
                Log.d(TAG,"duration: "+r.duration+"min");

                r.orig = orig;
                r.dest = dest;
                r.mode = Ride.MODE_DRIVE;
                r.uri = url.toString();
                
                r.price = -1;
                r.fun = 3;
                r.eco = 2;
                r.fast = 5;
                r.social = 2;
                r.green = 1;
                
           
                rides.add(r);
                Log.d(TAG, " + found "+r.uri);
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
