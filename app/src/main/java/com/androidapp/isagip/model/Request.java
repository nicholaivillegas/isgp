package com.androidapp.isagip.model;

/**
 * Created by Nico on 3/17/2017.
 */

public class Request {
    public String mobileNumber;
    public String email;
    public String date;
    public String longitude;
    public String latitude;
    public String location;
    public String food;
    public String clothes;
    public String medicine;
    public String familySize;
    public String personToContactName;
    public String personToContactNumber;
    public String others;
    public String status;
    public String operationId;
    public String id;
    public String foodRating;
    public String clothesRating;
    public String medicineRating;
    public String othersRating;
    public String calamity;

    public Request() {

    }

    public Request(String mobileNumber, String email, String date, String longitude, String latitude, String location, String food, String clothes, String medicine, String familySize, String personToContactName, String personToContactNumber, String others, String status, String operationId, String id, String foodRating, String clothesRating, String medicineRating, String othersRating, String calamity) {
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
        this.food = food;
        this.clothes = clothes;
        this.medicine = medicine;
        this.familySize = familySize;
        this.personToContactName = personToContactName;
        this.personToContactNumber = personToContactNumber;
        this.others = others;
        this.status = status;
        this.operationId = operationId;
        this.id = id;
        this.foodRating = foodRating;
        this.clothesRating = clothesRating;
        this.medicineRating = medicineRating;
        this.othersRating = othersRating;
        this.calamity = calamity;
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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
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

    public String getFamilySize() {
        return familySize;
    }

    public void setFamilySize(String familySize) {
        this.familySize = familySize;
    }

    public String getPersonToContactName() {
        return personToContactName;
    }

    public void setPersonToContactName(String personToContactName) {
        this.personToContactName = personToContactName;
    }

    public String getPersonToContactNumber() {
        return personToContactNumber;
    }

    public void setPersonToContactNumber(String personToContactNumber) {
        this.personToContactNumber = personToContactNumber;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoodRating() {
        return foodRating;
    }

    public void setFoodRating(String foodRating) {
        this.foodRating = foodRating;
    }

    public String getClothesRating() {
        return clothesRating;
    }

    public void setClothesRating(String clothesRating) {
        this.clothesRating = clothesRating;
    }

    public String getMedicineRating() {
        return medicineRating;
    }

    public void setMedicineRating(String medicineRating) {
        this.medicineRating = medicineRating;
    }

    public String getOthersRating() {
        return othersRating;
    }

    public void setOthersRating(String othersRating) {
        this.othersRating = othersRating;
    }

    public String getCalamity() {
        return calamity;
    }

    public void setCalamity(String calamity) {
        this.calamity = calamity;
    }
}

