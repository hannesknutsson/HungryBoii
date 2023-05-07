package com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants;

import com.github.hannesknutsson.hungryboii.api.dataclasses.Dish;
import com.github.hannesknutsson.hungryboii.api.dataclasses.OpenHours;
import com.github.hannesknutsson.hungryboii.api.dataclasses.Time;
import com.github.hannesknutsson.hungryboii.api.enumerations.Weekday;
import com.github.hannesknutsson.hungryboii.api.exceptions.ParsingOutdated;
import com.github.hannesknutsson.hungryboii.api.exceptions.WebPageBroken;
import com.github.hannesknutsson.hungryboii.api.statichelpers.HttpHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;

import static com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus.*;
import static com.github.hannesknutsson.hungryboii.api.statichelpers.TimeHelper.getDayOfWeek;

public class Futurum extends SimpleRestaurant {

    private static final String targetUrl = "https://www.restaurangfuturum.se/dagens-lunch";
    private static final String filterQuery = "div#SITE_PAGES_TRANSITION_GROUP > div > div:eq(1) > div > div > div > section > div:eq(1) > div:eq(0) > div > div:eq(0) > div:eq(1) > p";

    public Futurum() {
        super("Futurum", targetUrl, new OpenHours(new Time(11, 0), new Time(13, 30)));
    }

    @Override
    public void refreshData() throws WebPageBroken, ParsingOutdated {
        Document webPage = HttpHelper.getWebPage(targetUrl);
        List<Element> elementList = filterWebpage(webPage, filterQuery);
        Map<Weekday, List<String>> mealsGroupedByDays = parseElementsToMealMap(elementList);

        if (mealsGroupedByDays.size() != 5) {
            throw new ParsingOutdated("The number of retrieved days in the menu were not 5");
        }

        List<String> todaysAlternatives = mealsGroupedByDays.get(getDayOfWeek());
        if (todaysAlternatives.isEmpty()) {
            throw new ParsingOutdated("The number of retrieved dishes for today were less than 0");
        }

        for (String alternative : todaysAlternatives) {
            availableDishes.add(new Dish(alternative));
        }
    }

    private List<Element> filterWebpage(Document toFilter, String filterQuery) {
        return toFilter.select(filterQuery);
    }

    private Map<Weekday, List<String>> parseElementsToMealMap(List<Element> elementList) {
        Map<Weekday, List<String>> mealsGroupedByDays = new HashMap<>();
        List<String> tmpList = null;
        List<String> sweDays = Arrays.asList("MÅNDAG", "TISDAG", "ONSDAG", "TORSDAG", "FREDAG");
        int dayCounter = 0;

        for (Element e : elementList) {
            if (dayCounter < 5 && e.text().contains(sweDays.get(dayCounter)))  {
                tmpList = new ArrayList<>();
                mealsGroupedByDays.put(Weekday.values()[dayCounter], tmpList);
                dayCounter++;
            } else if (tmpList != null && e.text().length() > 0 && !e.text().equals("Vegetariskt alternativ") && !e.text().equals("Vid allergi, fråga personalen.")) {
                if (Character.isUpperCase(e.text().charAt(0))) {
                    tmpList.add(e.text());
                } else if (!tmpList.isEmpty()) {
                    String previousDish = tmpList.remove(tmpList.size() - 1);
                    tmpList.add(previousDish + " " + e.text());
                }
            }
        }
        return mealsGroupedByDays;
    }
}
