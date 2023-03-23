package com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants;

import com.github.hannesknutsson.hungryboii.api.dataclasses.Dish;
import com.github.hannesknutsson.hungryboii.api.dataclasses.OpenHours;
import com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus;

import java.util.concurrent.CopyOnWriteArrayList;

import static com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus.UNINITIALIZED;
import static com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus.WEEKEND;

public abstract class SimpleRestaurant implements Restaurant {

    private final String name;
    private final String url;
    private final OpenHours openHours;

    protected RestaurantStatus status;
    protected CopyOnWriteArrayList<Dish> availableDishes;

    public SimpleRestaurant(String name, String url, OpenHours openHours) {
        this.name = name;
        this.url = url;
        this.openHours = openHours;
        availableDishes = new CopyOnWriteArrayList<>();
        status = UNINITIALIZED;
    }

    @Override
    public final CopyOnWriteArrayList<Dish> getTodaysDishes() {
        return availableDishes;
    }

    @Override
    public void resetForWeekend() {
        availableDishes.clear();
        status = WEEKEND;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public OpenHours getOpenHours() {
        return openHours;
    }

    @Override
    public RestaurantStatus getStatus() {
        return status;
    }

    @Override
    public String getRestaurantInfo() {
        return "Open: " + getOpenHours();
    }
}
