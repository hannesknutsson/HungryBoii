package com.github.hannesknutsson.hungryboii.api;

import com.github.hannesknutsson.hungryboii.api.dataclasses.Dish;
import com.github.hannesknutsson.hungryboii.api.dataclasses.json.Block;
import com.github.hannesknutsson.hungryboii.api.dataclasses.json.Blocks;
import com.github.hannesknutsson.hungryboii.api.dataclasses.json.Message;
import com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants.abstractions.Restaurant;
import com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus;
import com.github.hannesknutsson.hungryboii.api.managers.implementations.RestaurantManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.github.hannesknutsson.hungryboii.api.dataclasses.json.Blocks.buttonElement;
import static com.github.hannesknutsson.hungryboii.api.dataclasses.json.Blocks.divider;
import static com.github.hannesknutsson.hungryboii.api.dataclasses.json.Blocks.markdownSection;
import static java.lang.String.format;

public class ListMenu {

    public String getTextMenus() {
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

    public String getSlackMenus() {
        List<Restaurant> restaurants = RestaurantManager.getInstance().getRegisteredRestaurants();
        List<Block> blocks = new ArrayList<>();
        blocks.add(markdownSection("*Todays lunch* :fork_and_knife:"));
        blocks.add(divider());
        String[] numbers = {"one", "two", "three", "four", "five", "six", "seven", "eighth", "nine"};
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getStatus().equals(RestaurantStatus.OK)) {
                String restaurantInfo = "Open: " + restaurant.getOpenHours() + " | Price: " + restaurant.getPrice() + ":-\n";
                StringBuilder dishes = new StringBuilder();
                for (Dish dish : restaurant.getTodaysDishes()) {
                    dishes.append("\t* ").append(dish.name).append("\n");
                }
                int index = restaurants.indexOf(restaurant);
                String sectionText = format(":%s:  *<%s|%s>*\n%s\n%s", numbers[index], restaurant.getUrl(), restaurant.getName(), restaurantInfo, dishes);
                blocks.add(markdownSection(sectionText));
            }
        }
        blocks.add(Blocks.actions(buttonElement()));

        return new Gson().toJson(Message.home(blocks));
    }

    private String compileMenu(Restaurant menuSource) {

        StringBuilder alternativeDescriptionBuilder = new StringBuilder();
        String restaurantInfo = "Open: " + menuSource.getOpenHours() + " | Price: " + menuSource.getPrice() + ":-\n";
        alternativeDescriptionBuilder.append(restaurantInfo);

        for (Dish dish : menuSource.getTodaysDishes()) {
            alternativeDescriptionBuilder.append("    * ").append(dish.name).append("\n");
        }
        return format("%s: %s", menuSource.getName(), alternativeDescriptionBuilder);
    }
}
