package com.github.hannesknutsson.hungryboii.structure.templates;

import com.github.hannesknutsson.hungryboii.structure.classes.Dish;
import com.github.hannesknutsson.hungryboii.structure.exceptions.CouldNotRefreshException;

import java.util.concurrent.CopyOnWriteArrayList;

public interface Restaurant {

    String getName();

    CopyOnWriteArrayList<Dish> getTodaysDishes();

    void refreshData() throws CouldNotRefreshException;
}
