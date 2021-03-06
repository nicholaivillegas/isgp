package com.androidapp.isagip.model;

/**
 * Created by john.villegas on 06/08/2017.
 */

public class Feedback {

    public String id;
    public String timestamp;
    public String mobileNumber;
    public String food;
    public String clothes;
    public String medicine;
    public String others;
    public String othersRate;
    public String rating;
    public String comment;
    public String status;
    public String operationId;
    public String operationPlace;

    public Feedback() {

    }

    public Feedback(String id, String timestamp, String mobileNumber, String food, String clothes, String medicine, String others, String othersRate, String rating, String comment, String status, String operationId, String operationPlace) {
        this.id = id;
        this.timestamp = timestamp;
        this.mobileNumber = mobileNumber;
        this.food = food;
        this.clothes = clothes;
        this.medicine = medicine;
        this.others = others;
        this.othersRate = othersRate;
        this.rating = rating;
        this.comment = comment;
        this.status = status;
        this.operationId = operationId;
        this.operationPlace = operationPlace;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public String getOthersRate() {
        return othersRate;
    }

    public void setOthersRate(String othersRate) {
        this.othersRate = othersRate;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getOperationPlace() {
        return operationPlace;
    }

    public void setOperationPlace(String operationPlace) {
        this.operationPlace = operationPlace;
    }
}
