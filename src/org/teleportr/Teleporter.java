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

package org.teleportr;

import org.teleportr.model.Place;
import org.teleportr.model.Ride;
import org.teleportr.util.QueryMultiplexer;
import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

public class Teleporter extends Application {

	public static final String TAG = "Teleporter";
	public QueryMultiplexer multiplexer;
	public Place origin;
	public Place destination;

	
	public Teleporter() {
		super();
		Log.d(TAG, "onCreate");
		
// 		smartspace fallback 
	    origin = new Place();
	    origin.lat = 48803262; 
	    origin.lon = 9188745;
        origin.name = "Shackspace";
        origin.city = "München";
        origin.address = "Leopoldstr 7";
        origin.icon = R.drawable.shackspace;
        
        destination = new Place();
        destination.lat = 48740955; 
        destination.lon = 9100823;
        destination.name = "Droidcamp";
        destination.city = "München";
        destination.address = "Arnulfstr 12";
        destination.icon = R.drawable.droidcamp;
		
	}
	
	public void setOrigin(Place orig) {
		if (origin != null) reset();
		origin = orig;
	}
	
	public void setDestination(Place dest) {
		if (destination != null) reset();
		this.destination = dest;
	}
	
	public void beam() {
		
		if (origin!=null && destination!=null) {
			Log.d(TAG, "BEAMING..");
			if (multiplexer == null)
				multiplexer = new QueryMultiplexer(this);
			multiplexer.search(origin, destination);
		}
	}
	
	public void reset() {
		if (multiplexer != null) {
			multiplexer.rides.clear();
			multiplexer.latest.clear();
		}
		getContentResolver().notifyChange(Ride.URI, null);
	}
	
	public Ride[] getRides(Ride[] rides) {
		if (multiplexer != null) {
			multiplexer.removeOutdated();
			return multiplexer.rides.toArray(new Ride[multiplexer.rides.size()]);
		}
		else return rides;
	}
}
