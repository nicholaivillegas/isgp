package com.androidapp.isagip.model;

public class User {

    public String id;
    public String name;
    public String email;
    public String number;
    public String birthdate;
    public String org;
    public String position;
    public String type;
    public String status;
    public String mother;
    public String father;
    public String child1;
    public String child2;
    public String child3;
    public String familyCount;

    public User() {

    }

    public User(String id, String name, String email, String number, String birthdate, String org, String position, String type, String status, String mother, String father, String child1, String child2, String child3, String familyCount) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.number = number;
        this.birthdate = birthdate;
        this.org = org;
        this.position = position;
        this.type = type;
        this.status = status;
        this.mother = mother;
        this.father = father;
        this.child1 = child1;
        this.child2 = child2;
        this.child3 = child3;
        this.familyCount = familyCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getChild1() {
        return child1;
    }

    public void setChild1(String child1) {
        this.child1 = child1;
    }

    public String getChild2() {
        return child2;
    }

    public void setChild2(String child2) {
        this.child2 = child2;
    }

    public String getChild3() {
        return child3;
    }

    public void setChild3(String child3) {
        this.child3 = child3;
    }

    public String getFamilyCount() {
        return familyCount;
    }

    public void setFamilyCount(String familyCount) {
        this.familyCount = familyCount;
    }
}
