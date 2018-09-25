package com.ncapdevi.sample.fragments;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by SOOKMYUNG on 2018-02-23.
 */

public class Pharmacy_DTO {
    private double wgs84Lat;
    private double wgs84Lon;
    private String name;
    private String tel;

    public double get_wgs84Lat() {
        return wgs84Lat;
    }
    public void set_wgs84Lat(double wgs84Lat) {this.wgs84Lat = wgs84Lat;}
    public double get_wgs84Lon() {
        return wgs84Lon;
    }
    public void set_wgs84Lon(double wgs84Lon) {
        this.wgs84Lon = wgs84Lon;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTel() {return tel;}
    public void setTel(String tel) {this.tel = tel;}

}