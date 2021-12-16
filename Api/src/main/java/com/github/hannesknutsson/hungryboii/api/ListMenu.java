package com.github.hannesknutsson.hungryboii.api;

import com.github.hannesknutsson.hungryboii.api.dataclasses.Dish;
import com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants.abstractions.Restaurant;
import com.github.hannesknutsson.hungryboii.api.managers.implementations.RestaurantManager;

import java.util.List;

import static java.lang.String.format;

public class ListMenu {

    public String getMenus() {
        List<Restaurant> restaurants = RestaurantManager.getInstance().getRegisteredRestaurants();
        StringBuilder response = new StringBuilder();
        for (Restaurant restaurant : restaurants) {
            switch (restaurant.getStatus()) {
                case OK -> response.append(compileMenu(restaurant));
                case UNINITIALIZED -> response.append(format("%s: Has not yet been fetched from their website for the first time yet... You're one quick little bugger :)\n", restaurant.getName()));
                case WEBSITE_BROKEN -> response.append(format("%s: Seems to be having technical difficulties... (Probably my fault haha whatever)\n", restaurant.getName()));
                case PARSING_BROKEN -> response.append(format("%s: The parsing for this restaurants website has broken. Why do they update that sort of stuff anyway? (my fault, not theirs)\n", restaurant.getName()));
                case WEEKEND -> response.append(format("%s: I do not provide lunch alternatives on weekdays. You should never see this message in the wild...\n", restaurant.getName()));
                default -> response.append(format("%s: If you see this, something has gone terribly wrong...\n", restaurant.getName()));
            }
        }

        return response.toString();
    }

    private String compileMenu(Restaurant menuSource) {

        StringBuilder alternativeDescriptionBuilder = new StringBuilder();
        String restaurantInfo = "Open: " + menuSource.getOpenHours() + " | Price: " + menuSource.getPrice() + ":-\n";
        alternativeDescriptionBuilder.append(restaurantInfo);

        for (Dish dish : menuSource.getTodaysDishes()) {
            alternativeDescriptionBuilder.append("    * ").append(dish.name).append("\n");
        }
        return format("%s: %s", menuSource.getName(), alternativeDescriptionBuilder.toString());
    }
}
