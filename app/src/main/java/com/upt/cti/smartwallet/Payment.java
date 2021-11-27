package com.upt.cti.smartwallet;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Payment {

    public String timestamp;
    private double cost;
    private String name;
    private String type;

    public Payment() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public String getType() {
        return type;
    }
}