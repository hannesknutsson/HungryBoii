package com.github.hannesknutsson.hungryboii.discord.structure.dataclasses;

public class OpenHours {

    private Time opens;
    private Time closes;

    public OpenHours(Time opens, Time closes) {
        this.opens = opens;
        this.closes = closes;
    }

    @Override
    public String toString() {
        return opens.toString() + " - " + closes.toString();
    }
}
