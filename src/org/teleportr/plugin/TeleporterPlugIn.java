/**
 * Copyright (c) 2010: <http://www.teleportr.org/> All rights reserved.
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

package org.teleportr.plugin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.teleportr.model.Place;
import org.teleportr.model.Ride;
import android.content.Intent;


public class TeleporterPlugIn implements IPlugIn {

    @Override
    public ArrayList<Ride> find(Place o, Place d, Date time) {

        ArrayList<Ride> rides = new ArrayList<Ride>();
        
        Ride r = new Ride();
      
        r = new Ride();
        r.orig = o;
        r.dest = d;
        r.mode = Ride.MODE_TELEPORTER;
        
        r.fun = 5;
        r.eco = 5;
        r.fast = 5;
        r.social = 1;
        r.green = 3;
        
        r.uri = "beam://disco";
        
        rides.add(r);
        
        return rides;
    }

	@Override
	public void share(Ride offer) {
		// TODO Auto-generated method stub
	}

}
