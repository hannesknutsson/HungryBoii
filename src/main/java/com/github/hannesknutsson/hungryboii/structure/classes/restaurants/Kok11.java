package com.github.hannesknutsson.hungryboii.structure.classes.restaurants;

import com.github.hannesknutsson.hungryboii.structure.classes.Dish;
import com.github.hannesknutsson.hungryboii.structure.exceptions.CouldNotRefreshException;
import com.github.hannesknutsson.hungryboii.structure.templates.Restaurant;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.HttpHelper;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Kok11 implements Restaurant {

    private static final String name = "Kök 11";
    private static final String targetUrl = "http://www.kok11.se/dagenslunch-vaxjo";

    CopyOnWriteArrayList<Dish> availableDishes;

    public Kok11() {
        availableDishes = new CopyOnWriteArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CopyOnWriteArrayList<Dish> getTodaysDishes() {
        return availableDishes;
    }

    @Override
    public void refreshData() throws CouldNotRefreshException {

        List<Element> elementlist;
        try {
            elementlist = HttpHelper.getDocumentPage(targetUrl).select("div > div > div > div > div > h2");
        } catch (IOException e) {
            throw new CouldNotRefreshException("Futurum failed to refresh!", e);
        }

        List<String> alternatives = elementlist.stream().flatMap(e -> e.childNodesCopy().stream()).map(Node::toString).collect(Collectors.toList());
        List<Dish> dishes = alternatives.stream().map(a -> new Dish(a)).collect(Collectors.toList());

        availableDishes.clear();
        availableDishes.addAll(dishes);
    }
}
