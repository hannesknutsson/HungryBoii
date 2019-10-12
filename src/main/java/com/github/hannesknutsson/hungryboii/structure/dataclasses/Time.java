package com.github.hannesknutsson.hungryboii.structure.dataclasses;

public class Time {

    private int hour;
    private int minute;

    private boolean correctInput;

    public Time(int hour, int minute) {
        correctInput = true;

        if (hour >= 0 && hour < 24) {
            this.hour = hour;
        } else {
            correctInput = false;
        }

        if (minute >= 0 && minute < 60) {
            this.minute = minute;
        } else {
            correctInput = false;
        }
    }

    @Override
    public String toString() {
        if (correctInput) {
            String minuteString = minute < 10 ? "0" + minute : minute + "";
            return hour + ":" + minuteString;
        } else {
            return "N/A";
        }
    }
}