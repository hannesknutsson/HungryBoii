package com.github.hannesknutsson.hungryboii.structure.classes.restaurants;

import com.github.hannesknutsson.hungryboii.structure.classes.Dish;
import com.github.hannesknutsson.hungryboii.structure.exceptions.CouldNotRefreshException;
import com.github.hannesknutsson.hungryboii.structure.templates.Restaurant;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.HttpHelper;
import org.jsoup.nodes.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.github.hannesknutsson.hungryboii.utilities.statichelpers.TimeHelper.getDayOfWeek;

public class Futurum implements Restaurant {

    private static final String name = "Futurum";
    private static final String targetUrl = "https://eurest.mashie.com/public/menu/restaurang+futurum/9ab27099?country=se";

    CopyOnWriteArrayList<Dish> availableDishes;

    public Futurum() {
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
            elementlist = HttpHelper.getDocumentPage(targetUrl).select("div:eq(0) > div:eq(0) > span.day:eq(0), div > section.day-alternative > strong > span");
        } catch (IOException e) {
            throw new CouldNotRefreshException("Futurum failed to refresh!", e);
        }

        Map<Integer, List<String>> mealsSortedByDays = new HashMap<>();

        List<String> tmpList = null;
        int i = 0;
        for (Element e : elementlist) {
            if (!e.hasClass("container-week")) {
                if (e.hasClass("day")) {
                    tmpList = new ArrayList<>();
                    mealsSortedByDays.put(i++, tmpList);
                } else {
                    if (tmpList != null)
                        tmpList.add(e.text());
                }
            }
        }

        List<String> todaysAlternatives = mealsSortedByDays.get(getDayOfWeek());
        availableDishes.clear();

        if (todaysAlternatives != null) {
            for (String alternative : todaysAlternatives) {
                availableDishes.add(new Dish(alternative));
            }
        }
    }
}
