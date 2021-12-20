package com.github.hannesknutsson.hungryboii.discord.structure.dataclasses.restaurants.abstractions;

import com.github.hannesknutsson.hungryboii.discord.structure.dataclasses.Dish;
import com.github.hannesknutsson.hungryboii.discord.structure.dataclasses.OpenHours;
import com.github.hannesknutsson.hungryboii.discord.structure.enumerations.RestaurantStatus;

import java.util.concurrent.CopyOnWriteArrayList;

public interface Restaurant  {

    String toString();

    String getName();

    OpenHours getOpenHours();

    int getPrice();

    CopyOnWriteArrayList<Dish> getTodaysDishes();

    RestaurantStatus getStatus();

    void refreshData();
}
