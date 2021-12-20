package com.github.hannesknutsson.hungryboii.discord.utilities.managers.implementations;

import com.github.hannesknutsson.hungryboii.discord.structure.dataclasses.restaurants.abstractions.Restaurant;
import com.github.hannesknutsson.hungryboii.discord.utilities.managers.abstractions.ListingManager;

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
