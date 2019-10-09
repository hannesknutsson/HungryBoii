package com.github.hannesknutsson.hungryboii.structure.templates;

import com.github.hannesknutsson.hungryboii.structure.classes.Dish;
import com.github.hannesknutsson.hungryboii.structure.classes.OpenHours;
import com.github.hannesknutsson.hungryboii.structure.enumerations.RestaurantStatus;

import java.util.concurrent.CopyOnWriteArrayList;

public interface Restaurant {

    String getName();

    OpenHours getOpenHours();

    int getPrice();

    CopyOnWriteArrayList<Dish> getTodaysDishes();

    RestaurantStatus getStatus();

    void refreshData();
}
