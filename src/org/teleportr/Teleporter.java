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
	private QueryMultiplexer multiplexer;
	public Place origin;
	public Place destination;

	
	public Teleporter() {
		super();
		Log.d(TAG, "onCreate");
		
		// smartspace fallback 
//		origin = new Place();
//		origin.lat = 52512923; 
//		origin.lon = 13420555;
//        origin.name = "c-base";
//        origin.city = "Berlin";
//        origin.icon = R.drawable.cbase;
//        origin.address = "Rungestraße 20";
	}
	
	@Override
	public void onTerminate() {
		Log.d(Teleporter.TAG, "TERMINATING");
		super.onTerminate();
	}

	@Override
	public void onLowMemory() {
		Log.d(Teleporter.TAG, "LOW MEMORY");
		super.onLowMemory();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.d(Teleporter.TAG, "CONFIG CHANGE");
		super.onConfigurationChanged(newConfig);
	}

	public void setCurrentPlace(Place p) {
		origin = p;
	}

	public void beam() {
		
		Log.d(TAG, "BEAMING..");
		if (multiplexer == null)
			multiplexer = new QueryMultiplexer(this);
		if (origin!=null && destination!=null) {
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
