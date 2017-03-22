package com.androidapp.isagip.model;

/**
 * Created by Nico on 3/17/2017.
 */

public class Request {

    public String email;
    public String date;
    public String location;
    public String food;
    public String clothes;
    public String medicine;
    public String others;

    public Request() {

    }

    public Request(String email, String date, String location, String food, String clothes, String medicine, String others) {
        this.email = email;
        this.date = date;
        this.location = location;
        this.food = food;
        this.clothes = clothes;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getClothes() {
        return clothes;
    }

    public void setClothes(String clothes) {
        this.clothes = clothes;
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
