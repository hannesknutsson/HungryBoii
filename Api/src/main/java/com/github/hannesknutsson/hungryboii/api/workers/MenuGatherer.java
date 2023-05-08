package com.github.hannesknutsson.hungryboii.api.workers;

import com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants.Restaurant;
import com.github.hannesknutsson.hungryboii.api.managers.implementations.RestaurantManager;
import com.github.hannesknutsson.hungryboii.api.statichelpers.TimeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class MenuGatherer {
    private static final Logger LOG = LoggerFactory.getLogger(MenuGatherer.class);

    @Scheduled(cron = "0 0 * * * *")
    public static void refreshRestaurant() {
        List<Restaurant> restaurants = RestaurantManager.getInstance().getRegisteredRestaurants();

        for (Restaurant restaurant : restaurants) {
            LOG.info("Refreshing menu for {}", restaurant.getName());
            if (TimeHelper.isWeekend()) {
                restaurant.resetForWeekend();
            } else {
                restaurant.updateMenu();
            }
        }
    }
}
