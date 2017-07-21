package com.androidapp.isagip.model;

/**
 * Created by john.villegas on 21/07/2017.
 */

public class AffectedArea {
    public Double latitude;
    public Double longitude;
    public String location;

    public AffectedArea() {
    }

    public AffectedArea(Double latitude, Double longitude, String location) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
