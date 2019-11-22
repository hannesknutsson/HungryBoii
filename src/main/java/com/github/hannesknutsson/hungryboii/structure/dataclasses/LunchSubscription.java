package com.github.hannesknutsson.hungryboii.structure.dataclasses;

import javax.persistence.*;

@Entity
@Table(name = "lunchsubscription")
public class LunchSubscription {

    public LunchSubscription() {}

    public LunchSubscription(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int hour;

    private int minute;

    public Time getTime() {
        return new Time(hour, minute);
    }

    @Override
    public String toString() {
        return new Time(hour, minute).toString();
    }
}
