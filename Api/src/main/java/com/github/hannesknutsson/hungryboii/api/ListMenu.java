package com.github.hannesknutsson.hungryboii.api;

import com.github.hannesknutsson.hungryboii.api.dataclasses.Dish;
import com.github.hannesknutsson.hungryboii.api.dataclasses.json.Block;
import com.github.hannesknutsson.hungryboii.api.dataclasses.json.Blocks;
import com.github.hannesknutsson.hungryboii.api.dataclasses.json.MenuMessage;
import com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants.abstractions.Restaurant;
import com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus;
import com.github.hannesknutsson.hungryboii.api.managers.implementations.RestaurantManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.github.hannesknutsson.hungryboii.api.dataclasses.json.Blocks.*;
import static java.lang.String.format;

public class ListMenu {

    public static String getTextMenus() {
        List<Restaurant> restaurants = RestaurantManager.getInstance().getRegisteredRestaurants();
        StringBuilder response = new StringBuilder();
        for (Restaurant restaurant : restaurants) {
            switch (restaurant.getStatus()) {
                case OK -> response.append(getTextMenuEntry(restaurant));
                case UNINITIALIZED -> response.append(format("%s: Has not yet been fetched from their website for the first time yet... You're one quick little bugger :)\n", restaurant.getName()));
                case WEBSITE_BROKEN -> response.append(format("%s: Seems to be having technical difficulties... (Probably my fault haha whatever)\n", restaurant.getName()));
                case PARSING_BROKEN -> response.append(format("%s: The parsing for this restaurants website has broken. Why do they update that sort of stuff anyway? (my fault, not theirs)\n", restaurant.getName()));
                case WEEKEND -> response.append(format("%s: I do not provide lunch alternatives on weekdays. You should never see this message in the wild...\n", restaurant.getName()));
                default -> response.append(format("%s: If you see this, something has gone terribly wrong...\n", restaurant.getName()));
            }
        }

        return response.toString();
    }

    private static List<Block> commonBlocks() {
        List<Restaurant> restaurants = RestaurantManager.getInstance().getRegisteredRestaurants();
        List<Block> blocks = new ArrayList<>();
        blocks.add(header("Today's lunch:fork_and_knife:"));

        for (Restaurant restaurant : restaurants) {
            if (restaurant.getStatus().equals(RestaurantStatus.OK)) {
                int index = restaurants.indexOf(restaurant);
                String sectionText = getSlackMenuEntry(index, restaurant);
                blocks.add(markdownSection(sectionText));
            }
        }

        return blocks;
    }

    public static String showSlackMenu() {
        List<Block> blocks = commonBlocks();
        blocks.add(Blocks.actions(shareButton(), closeButton()));

        return new Gson().toJson(MenuMessage.show(blocks));
    }

    public static String shareSlackMenu() {
        List<Block> blocks = commonBlocks();
        blocks.add(Blocks.actions(closeButton()));

        return new Gson().toJson(MenuMessage.share(blocks));
    }

    private static String getTextMenuEntry(Restaurant restaurant) {
        return format("%s: %s%s", restaurant.getName(), restaurant.getRestaurantInfo(), getFoodList(restaurant));
    }

    private static String getSlackMenuEntry(int index, Restaurant restaurant) {
        String[] numbers = {"one", "two", "three", "four", "five", "six", "seven", "eighth", "nine"};
        String counterEmoji = format(":%s:  ", numbers[index]);
        return format("%s*<%s|%s>*\n%s\n%s", counterEmoji, restaurant.getUrl(), restaurant.getName(), restaurant.getRestaurantInfo(), getFoodList(restaurant));
    }

    private static String getFoodList(Restaurant restaurant) {
        StringBuilder dishes = new StringBuilder();
        for (Dish dish : restaurant.getTodaysDishes()) {
            dishes.append("\t• ").append(dish.name).append("\n");
        }
        return dishes.toString();
    }
}
