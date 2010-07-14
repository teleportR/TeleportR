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
		
		// smartspace fallback
		currentPlace = new Place();
        currentPlace.name = "c-base";
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
	
	public void reset() {
		if (multiplexer != null)
			multiplexer.rides.clear();
		getContentResolver().notifyChange(Ride.URI, null);
	}
	
	public Ride[] getRides() {
		return multiplexer.rides.toArray(new Ride[multiplexer.rides.size()]);
	}
}
