package com.github.hannesknutsson.hungryboii.structure.restaurants.abstractions;

import com.github.hannesknutsson.hungryboii.structure.dataclasses.Dish;
import com.github.hannesknutsson.hungryboii.structure.dataclasses.OpenHours;
import com.github.hannesknutsson.hungryboii.structure.enumerations.RestaurantStatus;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.github.hannesknutsson.hungryboii.structure.enumerations.RestaurantStatus.UNINITIALIZED;

public abstract class SimpleRestaurant implements Restaurant {

    private String name;
    private OpenHours openHours;
    private int price;

    protected RestaurantStatus status;
    protected CopyOnWriteArrayList<Dish> availableDishes;

    public SimpleRestaurant(String name, int price, OpenHours openHours) {
        this.name = name;
        this.openHours = openHours;
        this.price = price;
        availableDishes = new CopyOnWriteArrayList<>();
        status = UNINITIALIZED;
    }

    @Override
    public final CopyOnWriteArrayList<Dish> getTodaysDishes() {
        return availableDishes;
    }

    @Override
    public abstract void refreshData();

    @Override
    public String getName() {
        return name;
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
    public int getPrice() {
        return price;
    }

    @Override
    public RestaurantStatus getStatus() {
        return status;
    }
}
