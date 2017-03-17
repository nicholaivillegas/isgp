package com.androidapp.isagip.model;

/**
 * Created by Nico on 3/17/2017.
 */

public class Request {

    public String email;
    public String date;
    public String barangay;
    public String city;
    public String food;
    public String water;
    public String medicine;
    public String others;

    public Request() {

    }

    public Request(String email,String date, String barangay, String city, String food, String water, String medicine, String others) {
        this.email = email;
        this.date = date;
        this.barangay = barangay;
        this.city = city;
        this.food = food;
        this.water = water;
        this.medicine = medicine;
        this.others = others;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }
}
