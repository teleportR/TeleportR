package de.andlabs.teleporter;

import android.app.Application;
import android.util.Log;

public class Teleporter extends Application {

	public static final String TAG = "Teleporter";
	private QueryMultiplexer multiplexer;
	
	Place currentPlace;
	Place destination;

	
	
	public Teleporter() {
		super();
		
		// fallback
		currentPlace = new Place();
        currentPlace.name = "c-base";
        currentPlace.type = Place.TYPE_ADDRESS;
        currentPlace.address = "Rungestraße 20, Berlin";

	}

	public void beam() {
		
		Log.d(TAG, "BEAMING..");
		if (multiplexer == null) 
			multiplexer = new QueryMultiplexer(this);
		if (currentPlace!=null && destination!=null) {
			multiplexer.search(currentPlace, destination);
		}
	}
	
	public Ride[] getRides() {
		return multiplexer.rides.toArray(new Ride[multiplexer.rides.size()]);
	}


	
}
