package com.androidapp.isagip.model;

/**
 * Created by Nico on 3/17/2017.
 */

public class Request {
//o	food list down - can goods, rice, instant noodles. medicine - fever, colds, cough clothes- infant, young, adult
    public String mobileNumber;
    public String email;
    public String date;
    public String location;
    public String foodCannedGoods;
    public String foodRice;
    public String foodNoodles;
    public String clothesInfant;
    public String clothesYoung;
    public String clothesAdult;
    public String medicineFever;
    public String medicineColds;
    public String medicineCough;
    public String others;

    public Request() {

    }

    public Request(String mobileNumber, String email, String date, String location, String foodCannedGoods, String foodRice, String foodNoodles, String clothesInfant, String clothesYoung, String clothesAdult, String medicineFever, String medicineColds, String medicineCough, String others) {
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.date = date;
        this.location = location;
        this.foodCannedGoods = foodCannedGoods;
        this.foodRice = foodRice;
        this.foodNoodles = foodNoodles;
        this.clothesInfant = clothesInfant;
        this.clothesYoung = clothesYoung;
        this.clothesAdult = clothesAdult;
        this.medicineFever = medicineFever;
        this.medicineColds = medicineColds;
        this.medicineCough = medicineCough;
        this.others = others;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public String getFoodCannedGoods() {
        return foodCannedGoods;
    }

    public void setFoodCannedGoods(String foodCannedGoods) {
        this.foodCannedGoods = foodCannedGoods;
    }

    public String getFoodRice() {
        return foodRice;
    }

    public void setFoodRice(String foodRice) {
        this.foodRice = foodRice;
    }

    public String getFoodNoodles() {
        return foodNoodles;
    }

    public void setFoodNoodles(String foodNoodles) {
        this.foodNoodles = foodNoodles;
    }

    public String getClothesInfant() {
        return clothesInfant;
    }

    public void setClothesInfant(String clothesInfant) {
        this.clothesInfant = clothesInfant;
    }

    public String getClothesYoung() {
        return clothesYoung;
    }

    public void setClothesYoung(String clothesYoung) {
        this.clothesYoung = clothesYoung;
    }

    public String getClothesAdult() {
        return clothesAdult;
    }

    public void setClothesAdult(String clothesAdult) {
        this.clothesAdult = clothesAdult;
    }

    public String getMedicineFever() {
        return medicineFever;
    }

    public void setMedicineFever(String medicineFever) {
        this.medicineFever = medicineFever;
    }

    public String getMedicineColds() {
        return medicineColds;
    }

    public void setMedicineColds(String medicineColds) {
        this.medicineColds = medicineColds;
    }

    public String getMedicineCough() {
        return medicineCough;
    }

    public void setMedicineCough(String medicineCough) {
        this.medicineCough = medicineCough;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }
}
