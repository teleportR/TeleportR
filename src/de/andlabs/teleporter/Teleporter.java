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

		Log.d(TAG, "onCreate");
		
		// smartspace fallback 
		currentPlace = new Place();
		currentPlace.lat = 52512923; 
		currentPlace.lon = 13420555;
        currentPlace.name = "c-base";
        currentPlace.city = "Berlin";
        currentPlace.icon = R.drawable.cbase;
        currentPlace.address = "Rungestraße 20";
	}
	
	public void setCurrentPlace(Place p) {
		
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
	
	public Ride[] getRides(Ride[] rides) {
		if (multiplexer != null)
			return multiplexer.rides.toArray(new Ride[multiplexer.rides.size()]);
		else return rides;
	}
}
