package com.github.hannesknutsson.hungryboii.api;

import com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants.Kok11;
import com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants.MKCatering;
import com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants.Ostergatan;
import com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants.VidaArena;
import com.github.hannesknutsson.hungryboii.api.managers.implementations.RestaurantManager;
import com.github.hannesknutsson.hungryboii.api.workers.MenuGatherer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApiApplication {

    private static final Logger LOG = LoggerFactory.getLogger(ApiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
        // Register any new restaurants here (implementations of interface "Restaurant")
        RestaurantManager.getInstance().register(new Ostergatan());
        RestaurantManager.getInstance().register(new Kok11());
        RestaurantManager.getInstance().register(new VidaArena());
        RestaurantManager.getInstance().register(new MKCatering());

        MenuGatherer.refreshRestaurant();
    }

}
