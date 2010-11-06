package org.teleportr.plugin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.teleportr.R;
import org.teleportr.Teleporter;
import org.teleportr.model.Place;
import org.teleportr.model.Ride;

import android.util.Log;

public class TaxiPlugIn implements IPlugIn {
	

    @Override
    public ArrayList<Ride> find(Place o, Place d, Date time, Teleporter tlp) {

        ArrayList<Ride> rides = new ArrayList<Ride>();

        Ride r = new Ride();
        r.orig = o;
        r.dest = d;
        r.price = -1;
        
        for (Ride ride : tlp.multiplexer.rides) {
        	if (ride.mode == Ride.MODE_DRIVE) {
        		Log.d(Teleporter.TAG, "dist="+ride.distance);
        		r.duration = ride.duration;
        		r.distance = ride.distance;
        		float dist = r.distance / 1000; // km
        		r.price = 290;
        		if (dist < 5) {// first 5km
        			r.price += dist * 160;
        		} else {
        			r.price += 5 * 160;
        			if (dist < 10) {// 5km - 10km
        				r.price += (dist-5) * 140;
        			} else {
        				r.price += 5 * 140;
        				r.price += (dist-10) * 125;
        			}
        		}
        		continue;
        	}
        }
        r.mode = Ride.MODE_TAXI;
        r.fun = 1;
        r.eco = 1;
        r.fast = 4;
        r.social = 1;
        r.green = 1;
        rides.add(r);
        return rides;
      
    }

	@Override
	public void share(Ride offer) {
		// TODO Auto-generated method stub
	}

}
