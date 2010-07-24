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

package org.teleportr;

import org.teleportr.model.Place;
import org.teleportr.model.Ride;
import org.teleportr.util.QueryMultiplexer;
import android.app.Application;
import android.util.Log;

public class Teleporter extends Application {

	public static final String TAG = "Teleporter";
	private QueryMultiplexer multiplexer;
	public Place currentPlace;
	public Place destination;

	
	public Teleporter() {
		super();
		Log.d(TAG, "onCreate");
		
		// smartspace fallback 
//		currentPlace = new Place();
//		currentPlace.lat = 52512923; 
//		currentPlace.lon = 13420555;
//        currentPlace.name = "c-base";
//        currentPlace.city = "Berlin";
//        currentPlace.icon = R.drawable.cbase;
//        currentPlace.address = "Rungestraße 20";
	}
	
	public void setCurrentPlace(Place p) {
		currentPlace = p;
	}

	public void beam() {
		
		Log.d(TAG, "BEAMING..");
		if (multiplexer == null)
			multiplexer = new QueryMultiplexer(this);
		if (currentPlace!=null && destination!=null) {
			multiplexer.search(currentPlace, destination);
		}
	}
	
	public void reset() {
		if (multiplexer != null) {
			multiplexer.rides.clear();
			multiplexer.latest = null;
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
