package com.appttude.h_mal.days_left.Objects;

public class TimeObject {

    private String timeIn;
    private String timeOut;
    private int breakEpoch;
    private float hours;

    public TimeObject(String timeIn, String timeOut, int breakEpoch, float hours) {
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.breakEpoch = breakEpoch;
        this.hours = hours;
    }

    public TimeObject() {
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public int getBreakEpoch() {
        return breakEpoch;
    }

    public void setBreakEpoch(int breakEpoch) {
        this.breakEpoch = breakEpoch;
    }

    public float getHours() {
        return hours;
    }

    public void setHours(float hours) {
        this.hours = hours;
    }
}
