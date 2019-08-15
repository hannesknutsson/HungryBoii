package com.github.hannesknutsson.hungryboii.utilities.workers;

import com.github.hannesknutsson.hungryboii.structure.exceptions.CouldNotRefreshException;
import com.github.hannesknutsson.hungryboii.structure.templates.Restaurant;
import com.github.hannesknutsson.hungryboii.utilities.managers.RestaurantManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MenuGatherer {

    private static Logger LOG = LoggerFactory.getLogger(MenuGatherer.class);

    private static class Retriever implements Runnable {
        @Override
        public void run() {
            RestaurantManager.getRegisteredRestaurants().parallelStream().forEach(MenuGatherer::refreshRestaurant);
        }
    }

    private static ScheduledExecutorService executorService;
    private static Retriever gatheringTask;

    private static final int intervalTime = 10;
    private static final TimeUnit intervalUnit = TimeUnit.MINUTES;

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
        LOG.info("Refreshing menu of {}", target.getName());
        try {
            target.refreshData();
        } catch (CouldNotRefreshException e) {
            target.getTodaysDishes().clear(); //TODO lägg till restaurant status som ger olika meddelanden och så ;)
            LOG.error("Failed to refresh menu for {}", target.getName(), e);
        }
    }
}
