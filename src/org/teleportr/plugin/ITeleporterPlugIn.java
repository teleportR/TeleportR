package org.teleportr.plugin;

import java.util.ArrayList;
import java.util.Date;

import org.teleportr.Place;
import org.teleportr.Ride;


public interface ITeleporterPlugIn {

    public abstract ArrayList<Ride> find(Place orig, Place dest, Date time);

}