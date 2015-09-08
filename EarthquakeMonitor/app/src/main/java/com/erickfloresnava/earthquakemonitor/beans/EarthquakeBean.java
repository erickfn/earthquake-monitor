package com.erickfloresnava.earthquakemonitor.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by erickfloresnava on 9/6/15.
 */
public class EarthquakeBean implements Serializable{

    private String title;
    private String mag;
    private String place;
    private String time;
    private ArrayList<String> alCoordinates;

    public EarthquakeBean(String title, String mag, String place, String time, ArrayList<String> alCoordinates) {
        this.title = title;
        this.mag = mag;
        this.place = place;
        this.time = time;
        this.alCoordinates = alCoordinates;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMag() {
        return mag;
    }

    public void setMag(String mag) {
        this.mag = mag;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<String> getAlCoordinates() {
        return alCoordinates;
    }

    public void setAlCoordinates(ArrayList<String> alCoordinates) {
        this.alCoordinates = alCoordinates;
    }
}
