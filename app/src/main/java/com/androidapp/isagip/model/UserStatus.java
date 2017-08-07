package com.androidapp.isagip.model;

/**
 * Created by john.villegas on 07/08/2017.
 */

public class UserStatus {
    public String id;
    public String timestamp;
    public String transactionId;
    public String status;

    public UserStatus() {

    }

    public UserStatus(String id, String timestamp, String transactionId, String status) {
        this.id = id;
        this.timestamp = timestamp;
        this.transactionId = transactionId;
        this.status = status;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
