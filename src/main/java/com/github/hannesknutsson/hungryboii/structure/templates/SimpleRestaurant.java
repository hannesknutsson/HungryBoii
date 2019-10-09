package com.github.hannesknutsson.hungryboii.structure.templates;

import com.github.hannesknutsson.hungryboii.structure.classes.Dish;
import com.github.hannesknutsson.hungryboii.structure.classes.OpenHours;
import com.github.hannesknutsson.hungryboii.structure.enumerations.RestaurantStatus;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.github.hannesknutsson.hungryboii.structure.enumerations.RestaurantStatus.UNINITIALIZED;

public abstract class SimpleRestaurant implements Restaurant {

    private String name;
    private OpenHours openHours;

    protected RestaurantStatus status;
    protected CopyOnWriteArrayList<Dish> availableDishes;

    public SimpleRestaurant(String name, OpenHours openHours) {
        this.name = name;
        this.openHours = openHours;
        availableDishes = new CopyOnWriteArrayList<>();
        status = UNINITIALIZED;
    }

    @Override
    public final CopyOnWriteArrayList<Dish> getTodaysDishes() {     //TODO this mothod is rather ugly
        List<Dish> cleanAvaliableDishes = availableDishes.stream().map(dish -> new Dish(dish.name.replace("\n", " "))).collect(Collectors.toList());
        availableDishes.clear();
        availableDishes.addAll(cleanAvaliableDishes);
        return availableDishes;
    }

    @Override
    public abstract void refreshData();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public OpenHours getOpenHours() {
        return openHours;
    }

    @Override
    public RestaurantStatus getStatus() {
        return status;
    }
}
