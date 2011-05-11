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

import java.util.Date;

import org.teleportr.Teleporter;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Ride implements Parcelable {

	public static final Uri URI = Uri.parse("content://org.teleportr/rides");

	public final static int MODE_TELEPORTER = 0;
    public final static int MODE_SK8 = 1;
    public final static int MODE_TRANSIT = 2;
    public final static int MODE_FLIGHT = 3;
    public final static int MODE_TRAIN = 4;
    public final static int MODE_DRIVE = 5;
    public static final int MODE_BIKE = 6;
    public final static int MODE_WALK = 7;
    public final static int MODE_TAXI = 8;
    public final static int MODE_MFG = 9;

    public Place orig;
    public Place dest;
    public Date dep;
    public Date arr;
    public int mode;
    public int price;
    public String uri;
    public int distance;
    public long duration;

    // scoring
    public int fun;
    public int eco;
    public int fast;
    public int green;
    public int social;

    public Ride() {}
    
    public Ride(Parcel in) {
    	Log.d(Teleporter.TAG, "deserialize ride");
    	dep = (Date) in.readSerializable();
    	arr = (Date) in.readSerializable();
//		dep = new Date(in.readLong());
//		arr = new Date(in.readLong());
		duration = in.readLong();
		distance = in.readInt();
		uri = in.readString();
		price = in.readInt();
		mode = in.readInt();
	}
    
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeSerializable(dep);
		out.writeSerializable(arr);
//		out.writeLong((dep!=null)? dep.getTime() : 0);
//		out.writeLong((arr!=null)? arr.getTime() : 0);
		out.writeLong(duration);
		out.writeInt(distance);
		out.writeString(uri);
		out.writeInt(price);
		out.writeInt(mode);
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((arr == null) ? 0 : arr.hashCode());
        result = prime * result + ((dep == null) ? 0 : dep.hashCode());
        result = prime * result + ((dest == null) ? 0 : dest.hashCode());
        result = prime * result + (int) (duration ^ (duration >>> 32));
        result = prime * result + eco;
        result = prime * result + fast;
        result = prime * result + fun;
        result = prime * result + green;
        result = prime * result + mode;
        result = prime * result + ((orig == null) ? 0 : orig.hashCode());
        result = prime * result + price;
        result = prime * result + social;
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
        Ride other = (Ride) obj;
        if (arr == null) {
            if (other.arr != null)
                return false;
        } else if (!arr.equals(other.arr))
            return false;
        if (dep == null) {
            if (other.dep != null)
                return false;
        } else if (!dep.equals(other.dep))
            return false;
        if (dest == null) {
            if (other.dest != null)
                return false;
        } else if (!dest.equals(other.dest))
            return false;
        if (duration != other.duration)
            return false;
        if (eco != other.eco)
            return false;
        if (fast != other.fast)
            return false;
        if (fun != other.fun)
            return false;
        if (green != other.green)
            return false;
        if (mode != other.mode)
            return false;
        if (orig == null) {
            if (other.orig != null)
                return false;
        } else if (!orig.equals(other.orig))
            return false;
        if (price != other.price)
            return false;
        if (social != other.social)
            return false;
        return true;
    }

	@Override
	public int describeContents() {
		return 0;
	}
	
	
	public static final Parcelable.Creator<Ride> CREATOR = new Parcelable.Creator<Ride>() {
		
		public Ride createFromParcel(Parcel in) {
			return new Ride(in);
		}

		public Ride[] newArray(int size) {
			return new Ride[size];
		}
	};

}
