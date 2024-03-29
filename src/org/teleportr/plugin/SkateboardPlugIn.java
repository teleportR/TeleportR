package org.teleportr.plugin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.teleportr.Teleporter;
import org.teleportr.model.Place;
import org.teleportr.model.Ride;

public class SkateboardPlugIn implements IPlugIn {

    @Override
    public ArrayList<Ride> find(Place o, Place d, Date time, Teleporter tlp) {

        ArrayList<Ride> rides = new ArrayList<Ride>();
        
        Ride r = new Ride();
      
        r = new Ride();
        r.orig = o;
        r.dest = d;
        r.duration = (3+123)*60000;
        r.mode = Ride.MODE_SK8;
        
        r.fun = 5;
        r.eco = 5;
        r.fast = 1;
        r.social = 3;
        r.green = 5;
        rides.add(r);
        
        return rides;
    }

	@Override
	public void share(Ride offer) {
		// TODO Auto-generated method stub
	}

}
