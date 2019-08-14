package com.github.hannesknutsson.hungryboii.utilities.workers;

import com.github.hannesknutsson.hungryboii.structure.exceptions.CouldNotRefreshException;
import com.github.hannesknutsson.hungryboii.structure.templates.Restaurant;
import com.github.hannesknutsson.hungryboii.utilities.managers.RestaurantManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MenuGatherer {

    private static class Retriever implements Runnable {
        @Override
        public void run() {
            RestaurantManager.getRegisteredRestaurants().parallelStream().forEach(MenuGatherer::refreshRestaurant);
        }
    }

    private static ScheduledExecutorService executorService;
    private static Retriever gatheringTask;

    public static void startGathering() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadScheduledExecutor();
        }
        if (gatheringTask == null) {
            gatheringTask = new Retriever();
        }
        executorService.scheduleAtFixedRate(gatheringTask, 0, 10, TimeUnit.MINUTES);
    }

    private MenuGatherer() {
    }

    private static void refreshRestaurant(Restaurant target) {
        try {
            target.refreshData();
        } catch (CouldNotRefreshException e) {
            target.getTodaysDishes().clear(); //TODO lägg till restaurant status som ger olika meddelanden och så ;)
        }
    }
}
