package de.andlabs.teleporter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class Place {

    public static final String CONTENT_TYPE = "foo";
    public static final Uri CONTENT_URI = Uri.parse("content://de.andlabs.teleporter/places");
    public static final String[] PROJECTION = new String[] {"name", "icon"};
    
    public static final int TYPE_STATION = 0;
    public static final int TYPE_ADDRESS = 1;
    
    
    public static Place find(Uri uri, Context ctx) {
    	Place p = new Place();
    	Cursor c = ctx.getContentResolver().query(uri, PROJECTION, null, null, null);
    	if (c.getCount() > 0) {
    		c.moveToFirst();
    		p.name = c.getString(0);
//    		p.address = c.getString(2);
    		switch (c.getInt(1)) {
        		case R.drawable.a_bus:
        			p.type = Place.TYPE_STATION;
        			break;
        		case R.drawable.a_boot:
        			p.type = Place.TYPE_STATION;
        			break;
        		case R.drawable.a_sbahn:
        			p.type = Place.TYPE_STATION;
        			break;
        		case R.drawable.a_tram:
        			p.type = Place.TYPE_STATION;
        			break;
        		case R.drawable.a_ubahn:
        			p.type = Place.TYPE_STATION;
        			break;
        		case R.drawable.a_zug:
        			p.type = Place.TYPE_STATION;
        			break;
				default:
					break;
			}
    	}
    	return p;
	}
    
    
    public int lat;
    public int lon;
    public int type;
    public String name;
    public String address;
	public String city;
    
    // multiple external IDs FROM foreign provider
    // links
    // 
    
    
    
    
    
    
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + lat;
        result = prime * result + lon;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + type;
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Place other = (Place) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (lat != other.lat)
            return false;
        if (lon != other.lon)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

}
