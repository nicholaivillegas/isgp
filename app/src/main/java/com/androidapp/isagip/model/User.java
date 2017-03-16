package com.androidapp.isagip.model;

/**
 * Created by Nico on 3/15/2017.
 */

public class User {

    public String id;
    public String email;
    public String number;
    public String birthdate;
    public String type;

    public User() {

    }

    public User(String id, String email, String number, String birthdate, String type) {
        this.id = id;
        this.email = email;
        this.number = number;
        this.birthdate = birthdate;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
