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

package org.teleportr.model;

import java.net.URLDecoder;

import org.teleportr.R;
import org.teleportr.Teleporter;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.util.Log;

public class Place implements Parcelable, BaseColumns {

	public int lat;
    public int lon;
    public int icon;
    public String name;
	public String city;
    public String address;

    public Place() {}
    
    public Place(Parcel in) {
    	lat = in.readInt();
    	lon = in.readInt();
    	icon = in.readInt();
    	name = in.readString();
    	city = in.readString();
    	address = in.readString();
    }

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(lat);
		out.writeInt(lon);
		out.writeInt(icon);
		out.writeString(name);
		out.writeString(city);
		out.writeString(address);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + lat;
        result = prime * result + lon;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + icon;
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
        if (icon != other.icon)
            return false;
        return true;
    }

    
	@Override
	public String toString() {
		return "name:"+ name +
				" address:"+ address +
				" latlon:"+ lat+lon +
				" city:"+ city +
				" type:"+ icon;
	}






	public static final Uri CONTENT_URI = Uri.parse("content://org.teleportr/places");

	public static final String LAT = "lat";
	public static final String LON = "lon";
	public static final String ICON = "ICON";
	public static final String NAME = "name";
	public static final String CITY = "city";
	public static final String ADDRESS = "address";
	public static final String[] PROJECTION =
					new String[] { LAT, LON, ICON, NAME, CITY, ADDRESS };
    

	public static Place find(Uri uri, Context ctx) {
    	
		Place p = new Place();
    	
    	if (uri.getScheme().equals("content")) {
    		
    		Cursor c = ctx.getContentResolver().query(uri, PROJECTION, null, null, null);
    		if (c != null) {
    			if (c.getCount() > 0) {
    				c.moveToFirst();
    				p.lat = c.getInt(0);
    				p.lon = c.getInt(1);
    				p.icon = c.getInt(2);
    				p.name = c.getString(3);
    				p.city = c.getString(4);
    				p.address = c.getString(5);
    			}
    			c.close();
    		} else Log.d(Teleporter.TAG, "place not found!");
    		
    	} else if (uri.getScheme().equals("geo")) {
    		String query = uri.getQuery();
    		Log.d(Teleporter.TAG, "uri: "+uri);
    		Log.d(Teleporter.TAG, "query: "+query);
    		
    		if (query != null) {
    			// qype formating
    			String[] q = query.substring(2).split(", ");
    			if (q.length == 2) {
    				p.address = q[0].substring(q[0].indexOf(" "))+" "; // street
    				p.address += q[0].substring(0, q[0].indexOf(" ")); // number
    				Log.d(Teleporter.TAG, "address: "+p.address);
    				p.city = q[1].substring(0, q[1].indexOf(" "));
    				Log.d(Teleporter.TAG, "city: "+p.city);
    				p.name = q[1].substring(q[1].indexOf("(")+1, q[1].indexOf(")"));
    				Log.d(Teleporter.TAG, "name: "+p.name);
    			}
    		} else {
    			String[] latlon = uri.toString().split(":")[1].split(",");
    			if (latlon.length == 2) {
    				p.lat = (int) (Float.parseFloat(latlon[0])*1E6);
    				p.lon = (int) (Float.parseFloat(latlon[1])*1E6);
    				Log.d(Teleporter.TAG, "latlon: "+p.lat+","+p.lon);
    				p.name = "klicktel";
    			}
    		}

    	}
    	
    	return p;
	}
    
	public static Place find(String query, Context ctx) {
		Place p = new Place();
		String[] split = query.split(",");
		if (split.length == 2) 
			p.city = split[1].trim();
		p.name = split[0];
		
		// does it look like streetname with house number?
		if (p.name.matches("\\w+\\s+\\d+")) {
			p.icon = R.drawable.a_street;
			p.address = p.name;
		} else {
			p.icon = R.drawable.a_bus;
		}
		Log.d(Teleporter.TAG, "found place "+p);
		return p;
	}
	
	
	public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
		
		public Place createFromParcel(Parcel in) {
			return new Place(in);
		}

		public Place[] newArray(int size) {
			return new Place[size];
		}
	};


}
