package de.andlabs.teleporter;

import java.net.URLDecoder;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class Place {

    public static final String CONTENT_TYPE = "foo";
    public static final Uri CONTENT_URI = Uri.parse("content://de.andlabs.teleporter/places");
//    public static final String[] PROJECTION = new String[] {"name", "address", "lat", "lon", "icon"};
    public static final String[] PROJECTION = new String[] {"name", "icon", "lat", "lon"};
    
    public static final int TYPE_ADDRESS = 0;
    public static final int TYPE_STATION = 1;
    
    
    public static Place find(Uri uri, Context ctx) {
    	Place p = new Place();
    	
    	if (uri.getScheme().equals("content")) {
    		
    		Cursor c = ctx.getContentResolver().query(uri, PROJECTION, null, null, null);
    		if (c.getCount() > 0) {
    			c.moveToFirst();
    			if (c.getInt(1) != R.drawable.a_street) 
    				p.name = c.getString(0);
    			else
    				p.address = p.name+" 23";
    			p.lat = c.getInt(2);
    			p.lon = c.getInt(3);
    		}
    		c.close();
    		
    	} else if (uri.getScheme().equals("geo")) {
    		String query = URLDecoder.decode(uri.toString().substring(10));
    		
            String[] q = query.split(",");
            if (Character.isDigit(query.charAt(0))) {
                p.address = q[0].substring(q[0].indexOf(" ")+1) +" "+ q[0].substring(0, q[0].indexOf(" "))+", "+q[1].split(" ")[1];
            } else {
                p.address = q[0]+", "+ q[1].split(" ")[2];
            }
            p.name = p.address;
    	}
    	
    	return p;
	}
    
	public static Place find(String query, Context ctx) {
		Place p = new Place();
		p.name = query;
		return p;
	}
    
    
    
    
    
    public int lat;
    public int lon;
    public int type;
    public String name;
    public String address;
	public String city;
    
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
