package com.github.hannesknutsson.hungryboii.api.workers;

import com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants.Restaurant;
import com.github.hannesknutsson.hungryboii.api.managers.implementations.RestaurantManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MenuGatherer {

    private static final Logger LOG = LoggerFactory.getLogger(MenuGatherer.class);

    private static class Retriever implements Runnable {
        @Override
        public void run() {
            RestaurantManager.getInstance().getRegisteredRestaurants().parallelStream().forEach(MenuGatherer::refreshRestaurant);
        }
    }

    private static ScheduledExecutorService executorService;
    private static Retriever gatheringTask;

    private static final int intervalTime = 1;
    private static final TimeUnit intervalUnit = TimeUnit.HOURS;

    public static void startGathering() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadScheduledExecutor();
        }

        if (gatheringTask == null) {
            gatheringTask = new Retriever();
        }

        LOG.info("Scheduling worker to refresh restaurant menus every {} {}s", intervalTime, intervalUnit.toString().toLowerCase());
        executorService.scheduleAtFixedRate(gatheringTask, 0, intervalTime, intervalUnit);
        LOG.info("Worker started");
    }

    private MenuGatherer() {
    }

    private static void refreshRestaurant(Restaurant target) {
        LOG.info("Refreshing menu for {}", target.getName());
        target.refreshData();
    }
}
