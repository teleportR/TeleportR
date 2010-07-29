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

package org.teleportr.plugin;

import java.util.Date;
import java.util.List;
import org.teleportr.model.Place;
import org.teleportr.model.Ride;


public interface IPlugIn {

	
    /**
     * Called to search for more rides.
     * 
     * @param orig  the origin to start from
     * @param dest  the destination to get to
     * @param time  the earliest departure time
     * @return a List of some found search results.
     */
    public abstract List<Ride> find(Place orig, Place dest, Date time);

    
    /**
     * Called to make a ride available.
     * 
     * @param offer  the ride to announce
     */
    public abstract void share(Ride offer);

    
}
