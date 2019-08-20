package com.github.hannesknutsson.hungryboii.structure.templates;

import com.github.hannesknutsson.hungryboii.structure.classes.Dish;
import com.github.hannesknutsson.hungryboii.structure.enumerations.RestaurantStatus;

import java.util.concurrent.CopyOnWriteArrayList;

import static com.github.hannesknutsson.hungryboii.structure.enumerations.RestaurantStatus.UNINITIALIZED;

public abstract class SimpleRestaurant implements Restaurant {

    private String name;
    protected RestaurantStatus status;
    protected CopyOnWriteArrayList<Dish> availableDishes;

    public SimpleRestaurant(String name) {
        this.name = name;
        availableDishes = new CopyOnWriteArrayList<>();
        status = UNINITIALIZED;
    }

    @Override
    public abstract CopyOnWriteArrayList<Dish> getTodaysDishes();

    @Override
    public abstract void refreshData();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public RestaurantStatus getStatus() {
        return status;
    }
}
