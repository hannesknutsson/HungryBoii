package com.github.hannesknutsson.hungryboii.structure.dataclasses;

import javax.persistence.*;

@Entity
@Table(name = "discorduser")
public class DiscordUser {

    public DiscordUser() {}

    public DiscordUser(long id) {
        this.id = id;
    }

    @Id
    private long id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "subscription_id")
    private LunchSubscription lunchSubscription;

    public LunchSubscription getLunchSubscription() {
        return lunchSubscription;
    }

    public void setLunchSubscription(LunchSubscription lunchSubscription) {
        this.lunchSubscription = lunchSubscription;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "" + id;
    }
}
