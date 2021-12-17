package com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants.abstractions;

import com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus;
import com.github.hannesknutsson.hungryboii.api.dataclasses.Dish;
import com.github.hannesknutsson.hungryboii.api.dataclasses.OpenHours;

import java.util.concurrent.CopyOnWriteArrayList;

public interface Restaurant  {

    String toString();

    String getName();

    String getUrl();

    OpenHours getOpenHours();

    int getPrice();

    CopyOnWriteArrayList<Dish> getTodaysDishes();

    RestaurantStatus getStatus();

    void refreshData();

    String getRestaurantInfo();
}
