package com.github.hannesknutsson.hungryboii.utilities.managers;

import com.github.hannesknutsson.hungryboii.structure.templates.Restaurant;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RestaurantManager {

    private static List<Restaurant> restaurants;

    public static void registerRestaurant(Restaurant toRegister) {
        getRegisteredRestaurants().add(toRegister);
    }

    public static boolean unregisterRestaurant(Object toUnregister) {
        return getRegisteredRestaurants().remove(toUnregister);
    }

    public static List<Restaurant> getRegisteredRestaurants() {
        if (restaurants == null) {
            restaurants = new CopyOnWriteArrayList<>();
        }
        return restaurants;
    }
}
