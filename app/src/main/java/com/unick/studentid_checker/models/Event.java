package com.unick.studentid_checker.models;

/**
 * Created by unick on 2018/4/8.
 */
public class Event {
    public String userID;
    public String schoolname;
    public String color1;
    public int amountOfColor1;
    public int stock1;
    public String color2;
    public int amountOfColor2;
    public int stock2;
    public String color3;
    public int amountOfColor3;
    public int stock3;

    public Event() {
        // Default constructor required for calls to DataSnapshot.getValue(Event.class)
    }

    public Event(String userID,String schoolname,
                 String color1, int amountOfColor1, int stock1,
                 String color2, int amountOfColor2, int stock2,
                 String color3, int amountOfColor3, int stock3) {
        this.userID = userID;
        this.schoolname = schoolname;
        this.color1 = color1;
        this.amountOfColor1 = amountOfColor1;
        this.stock1 = stock1;
        this.color2 = color2;
        this.amountOfColor2 = amountOfColor2;
        this.stock2 = stock2;
        this.color3 = color3;
        this.amountOfColor3 = amountOfColor3;
        this.stock3 = stock3;
    }
}
