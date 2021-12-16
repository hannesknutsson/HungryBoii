package com.github.hannesknutsson.hungryboii.api.managers.implementations;

import com.github.hannesknutsson.hungryboii.api.managers.abstractions.ListingManager;
import com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants.abstractions.Restaurant;

import java.util.List;

public class RestaurantManager extends ListingManager<Restaurant> {

    private static RestaurantManager restaurantManager;

    public List<Restaurant> getRegisteredRestaurants() {
        return getRegisteredObjects();
    }

    @Override
    public String getManagerType() {
        return "Restaurant";
    }

    public static RestaurantManager getInstance() {
        if (restaurantManager == null) {
            restaurantManager = new RestaurantManager();
        }
        return restaurantManager;
    }
}
