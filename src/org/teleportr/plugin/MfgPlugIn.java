package org.teleportr.plugin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.teleportr.R;
import org.teleportr.model.Place;
import org.teleportr.model.Ride;

public class MfgPlugIn implements IPlugIn {

    @Override
    public ArrayList<Ride> find(Place o, Place d, Date time) {

        ArrayList<Ride> rides = new ArrayList<Ride>();
        
     // mfg
        Ride r = new Ride();
        r.orig = o;
        r.dest = d;
        r.dep = new Date(System.currentTimeMillis()+7*60000);
        r.arr = new Date(System.currentTimeMillis()+(7+22)*60000);
        r.mode = Ride.MODE_MFG;
        r.price = 150;
        r.fun = 3;
        r.eco = 5;
        r.fast = 3;
        r.social = 4;
        r.green = 3;
        rides.add(r);

        // taxi teiler
        r = new Ride();
        r.orig = o;
        r.dest = d;
        r.dep = new Date(System.currentTimeMillis()+8*60000);
        r.arr = new Date(System.currentTimeMillis()+(8+22)*60000);
        r.mode = Ride.MODE_MFG;
        r.price = 320;
        r.fun = 3;
        r.eco = 4;
        r.fast = 3;
        r.social = 5;
        r.green = 3;
        rides.add(r);
        
        return rides;
    }

	@Override
	public List<Ride> share(Ride offer) {
		// TODO Auto-generated method stub
		return null;
	}

}
