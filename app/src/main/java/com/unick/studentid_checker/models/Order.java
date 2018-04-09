package com.unick.studentid_checker.models;

/**
 * Created by unick on 2018/4/9.
 */

public class Order {
    public String nfc_ID;
    public String color;

    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(Event.class)
    }

    public Order(String nfc_ID, String color) {
        this.nfc_ID = nfc_ID;
        this.color = color;
    }
}
