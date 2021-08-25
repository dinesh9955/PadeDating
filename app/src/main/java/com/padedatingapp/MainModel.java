package com.padedatingapp;

import java.util.ArrayList;

public class MainModel {
    String trip_id = "";
    String start_time = "";
    String end_time = "";
    ArrayList<LocationsModel> locations = new ArrayList<>();

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public ArrayList<LocationsModel> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<LocationsModel> locations) {
        this.locations = locations;
    }
}
