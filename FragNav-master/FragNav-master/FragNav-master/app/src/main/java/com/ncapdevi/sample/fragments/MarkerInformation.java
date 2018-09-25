package com.ncapdevi.sample.fragments;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by MinsungKu on 2018-03-06.
 */

public class MarkerInformation implements ClusterItem {
    private LatLng location;
    private String mTitle;
    private String mSnippet;

    public MarkerInformation(LatLng location, String t, String s) {
        this.location = location;
        this.mTitle = t;
        this.mSnippet = s;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location){
        this.location = location;
    }

    @Override
    public LatLng getPosition() {
        return location;
    }

    public String getTitle(){ return mTitle;}

    public String getSnippet(){ return mSnippet;}
}
