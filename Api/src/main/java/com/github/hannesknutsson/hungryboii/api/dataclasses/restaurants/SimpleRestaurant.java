package com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants;

import com.github.hannesknutsson.hungryboii.api.dataclasses.Dish;
import com.github.hannesknutsson.hungryboii.api.dataclasses.OpenHours;
import com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus;
import com.github.hannesknutsson.hungryboii.api.exceptions.ParsingOutdated;
import com.github.hannesknutsson.hungryboii.api.exceptions.WebPageBroken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

import static com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus.*;
import static com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus.PARSING_BROKEN;

public abstract class SimpleRestaurant implements Restaurant {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleRestaurant.class);
    private final String name;
    private final String url;
    private final OpenHours openHours;

    protected RestaurantStatus status;
    protected CopyOnWriteArrayList<Dish> availableDishes;

    public SimpleRestaurant(String name, String url, OpenHours openHours) {
        this.name = name;
        this.url = url;
        this.openHours = openHours;
        availableDishes = new CopyOnWriteArrayList<>();
        status = UNINITIALIZED;
    }

    public final void updateMenu() {
        availableDishes.clear();
        try {
            refreshData();
            status = OK;
        } catch (WebPageBroken e) {
            status = WEBSITE_BROKEN;
            LOG.error(this.name + " has encountered an error.", e);
        } catch (ParsingOutdated e) {
            status = PARSING_BROKEN;
            LOG.error(this.name + " has encountered an error.", e);
        }
    }

    protected abstract void refreshData() throws WebPageBroken, ParsingOutdated;

    @Override
    public final CopyOnWriteArrayList<Dish> getTodaysDishes() {
        return availableDishes;
    }

    @Override
    public void resetForWeekend() {
        availableDishes.clear();
        status = WEEKEND;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public OpenHours getOpenHours() {
        return openHours;
    }

    @Override
    public RestaurantStatus getStatus() {
        return status;
    }

    @Override
    public String getRestaurantInfo() {
        return "Open: " + getOpenHours();
    }
}
