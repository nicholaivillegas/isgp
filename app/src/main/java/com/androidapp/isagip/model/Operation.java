package com.androidapp.isagip.model;

/**
 * Created by john.villegas on 07/08/2017.
 */

public class Operation {

    public String area;
    public String created_by;
    public String date;
    public String description;
    public String location;
    public String latitude;
    public String longitude;
    public String relief_count;
    public String status;
    public String title;

    public Operation() {

    }

    public Operation(String area, String created_by, String date, String description, String location, String latitude, String longitude, String relief_count, String status, String title) {
        this.area = area;
        this.created_by = created_by;
        this.date = date;
        this.description = description;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.relief_count = relief_count;
        this.status = status;
        this.title = title;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getRelief_count() {
        return relief_count;
    }

    public void setRelief_count(String relief_count) {
        this.relief_count = relief_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
