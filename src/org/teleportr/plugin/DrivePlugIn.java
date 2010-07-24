package org.teleportr.plugin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.teleportr.R;
import org.teleportr.model.Place;
import org.teleportr.model.Ride;

public class DrivePlugIn implements IPlugIn {

    @Override
    public ArrayList<Ride> find(Place o, Place d, Date time) {

        ArrayList<Ride> rides = new ArrayList<Ride>();
        
        Ride r = new Ride();
        r.orig = o;
        r.dest = d;
        r.dep = new Date(System.currentTimeMillis()+2*60000);
        r.arr = new Date(System.currentTimeMillis()+(2+22)*60000);
        r.mode = Ride.MODE_DRIVE;
        r.fun = 1;
        r.eco = 1;
        r.fast = 5;
        r.social = 1;
        r.green = 1;
        rides.add(r);

        
        return rides;
    }

	@Override
	public List<Ride> share(Ride offer) {
		// TODO Auto-generated method stub
		return null;
	}

}
