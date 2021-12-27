package com.github.hannesknutsson.hungryboii.api;

import com.github.hannesknutsson.hungryboii.api.dataclasses.Dish;
import com.github.hannesknutsson.hungryboii.api.dataclasses.json.Block;
import com.github.hannesknutsson.hungryboii.api.dataclasses.json.Blocks;
import com.github.hannesknutsson.hungryboii.api.dataclasses.json.MenuMessage;
import com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants.Restaurant;
import com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus;
import com.github.hannesknutsson.hungryboii.api.managers.implementations.RestaurantManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.github.hannesknutsson.hungryboii.api.dataclasses.json.Blocks.*;
import static java.lang.String.format;

public class ListMenu {

    public static String getTxtMenus() {
        List<Restaurant> restaurants = RestaurantManager.getInstance().getRegisteredRestaurants();
        StringBuilder response = new StringBuilder();
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getStatus() == RestaurantStatus.OK) {
                response.append(getTextMenuEntry(restaurant));
            } else {
                response.append(format("%s: %s\n", restaurant.getName(), getErrorResponse(restaurant.getStatus())));
            }
        }
        return response.toString();
    }

    public static String getXmlMenus() {
        List<Restaurant> restaurants = RestaurantManager.getInstance().getRegisteredRestaurants();
        StringBuilder response = new StringBuilder();
        response.append("<menus>");
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getStatus() == RestaurantStatus.OK) {
                response.append(getXmlMenuEntry(restaurant));
            } else {
                response.append(format("<restaurant><name>%s</name><error>%s</error></restaurant>",
                        restaurant.getName(),
                        getErrorResponse(restaurant.getStatus())));
            }
        }
        response.append("</menus>");
        return response.toString();
    }

    private static String getErrorResponse(RestaurantStatus status) {
        return switch (status) {
            case UNINITIALIZED -> "Has not yet been fetched from their website for the first time yet.";
            case WEBSITE_BROKEN -> "Seems to be having technical difficulties.";
            case PARSING_BROKEN -> "The parsing for this restaurants website has broken.";
            case WEEKEND -> "I do not provide lunch alternatives on weekdays.";
            default -> "If you see this, something has gone terribly wrong...";
        };
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
        return format("%s: %s\n%s", restaurant.getName(), restaurant.getRestaurantInfo(), getFoodList(restaurant));
    }

    private static String getXmlMenuEntry(Restaurant restaurant) {
        StringBuilder dishes = new StringBuilder();
        for (Dish dish : restaurant.getTodaysDishes()) {
            dishes.append("<entry>").append(dish.name).append("</entry>");
        }
        return format("<restaurant><name>%s</name><info>%s</info><entries>%s</entries></restaurant>",
                restaurant.getName(),
                restaurant.getRestaurantInfo(),
                dishes);
    }

    private static String getSlackMenuEntry(int index, Restaurant restaurant) {
        String[] numbers = {"one", "two", "three", "four", "five", "six", "seven", "eighth", "nine"};
        String counterEmoji = format(":%s:  ", numbers[index]);
        return format("%s*<%s|%s>*\n%s\n\n%s", counterEmoji, restaurant.getUrl(), restaurant.getName(), restaurant.getRestaurantInfo(), getFoodList(restaurant));
    }

    private static String getFoodList(Restaurant restaurant) {
        StringBuilder dishes = new StringBuilder();
        for (Dish dish : restaurant.getTodaysDishes()) {
            dishes.append("\t• ").append(dish.name).append("\n");
        }
        return dishes.toString();
    }
}
