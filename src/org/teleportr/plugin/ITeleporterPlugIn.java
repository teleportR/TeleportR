package org.teleportr.plugin;

import java.util.ArrayList;
import java.util.Date;

import org.teleportr.model.Place;
import org.teleportr.model.Ride;


public interface ITeleporterPlugIn {

    public abstract ArrayList<Ride> find(Place orig, Place dest, Date time);

}