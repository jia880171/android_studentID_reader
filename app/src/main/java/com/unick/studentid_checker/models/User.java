package com.unick.studentid_checker.models;

/**
 * Created by unick on 2018/4/8.
 */

public class User {
    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(Event.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
