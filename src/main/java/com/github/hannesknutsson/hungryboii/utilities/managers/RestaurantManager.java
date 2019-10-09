package com.github.hannesknutsson.hungryboii.utilities.managers;

import com.github.hannesknutsson.hungryboii.structure.templates.Restaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RestaurantManager {

    private static Logger LOG = LoggerFactory.getLogger(RestaurantManager.class);

    private static List<Restaurant> restaurants;

    public static boolean registerRestaurant(Restaurant toRegister) {
        boolean success = false;
        if (!ApplicationManager.isRunning()) {
            getRegisteredRestaurants().add(toRegister);
            success = true;
            LOG.debug("Restaurant registered: {}", toRegister.getName());
        } else {
            LOG.error("Can not register restaurant after application start: {}", toRegister.getName());
        }
        return success;
    }

    public static boolean unregisterRestaurant(Restaurant toUnregister) {
        boolean success = false;
        if (!ApplicationManager.isRunning()) {
            success = getRegisteredRestaurants().remove(toUnregister);
            if (success) {
                LOG.debug("Restaurant unregistered: {}", toUnregister.getName());
            } else {
                LOG.error("Failed to unregister restaurant: {}", toUnregister.getName());
            }
        } else {
            LOG.error("Can not unregister restaurant after application start: {}", toUnregister);
        }
        return success;
    }

    public static List<Restaurant> getRegisteredRestaurants() {
        if (restaurants == null) {
            restaurants = new CopyOnWriteArrayList<>();
        }
        return restaurants;
    }
}
