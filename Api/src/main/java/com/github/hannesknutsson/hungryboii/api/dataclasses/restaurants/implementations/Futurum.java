package com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants.implementations;

import com.github.hannesknutsson.hungryboii.api.dataclasses.Dish;
import com.github.hannesknutsson.hungryboii.api.dataclasses.OpenHours;
import com.github.hannesknutsson.hungryboii.api.dataclasses.Time;
import com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants.abstractions.SimpleRestaurant;
import com.github.hannesknutsson.hungryboii.api.enumerations.Weekday;
import com.github.hannesknutsson.hungryboii.api.exceptions.ParsingOutdated;
import com.github.hannesknutsson.hungryboii.api.exceptions.TotallyBrokenDudeException;
import com.github.hannesknutsson.hungryboii.api.exceptions.WebPageBroken;
import com.github.hannesknutsson.hungryboii.api.statichelpers.HttpHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;

import static com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus.*;
import static com.github.hannesknutsson.hungryboii.api.statichelpers.TimeHelper.getDayOfWeek;

public class Futurum extends SimpleRestaurant {

    private static final String targetUrl = "https://www.restaurangfuturum.se/dagens-lunch";
//    private static final String filterQuery = "div:eq(0) > div:eq(0) > span.day:eq(0), div > section.day-alternative > strong > span";
    private static final String filterQuery = "div#TRANSITION_GROUP > div > div:eq(1) > div > div > div > section > div:eq(1) > div:eq(0) > div > div:eq(0) > div:eq(1) > p > span > span";

    public Futurum() {
        super("Futurum", 98, new OpenHours(new Time(11, 0), new Time(13, 30)));
    }

    @Override
    public void refreshData() {
        try {
            Document webPage = HttpHelper.getWebPage(targetUrl);
            List<Element> elementList = filterWebpage(webPage, filterQuery);
            System.out.println("elementList: " + Arrays.toString(elementList.toArray()));
            Map<Weekday, List<String>> mealsGroupedByDays = parseElementsToMealMap(elementList);
            List<String> todaysAlternatives = mealsGroupedByDays.get(getDayOfWeek());

            availableDishes.clear();

            //We should have five days worth of alternatives and more than one alternative for today to have succeeded
            if (mealsGroupedByDays.size() != 5 || todaysAlternatives.size() <= 0) {
                throw new ParsingOutdated();
            }

            for (String alternative : todaysAlternatives) {
                availableDishes.add(new Dish(alternative));
            }

            status = OK;

        } catch (WebPageBroken exception) {
            status = WEBSITE_BROKEN;
        } catch (ParsingOutdated | TotallyBrokenDudeException parsingOutdated) {
            status = PARSING_BROKEN;
        }
    }

    private List<Element> filterWebpage(Document toFilter, String filterQuery) {
        return toFilter.select(filterQuery);
    }

    private Map<Weekday, List<String>> parseElementsToMealMap(List<Element> elementList) {
        Map<Weekday, List<String>> mealsGroupedByDays = new HashMap<>();
        List<String> tmpList = null;
        List<Weekday> dayList = Arrays.asList(Weekday.MONDAY, Weekday.TUESDAY, Weekday.WEDNESDAY, Weekday.THURSDAY, Weekday.FRIDAY);
        int dayCounter = 0;

        for (Element e : elementList) {
            if (!e.hasClass("container-week")) {
                if (e.hasClass("day")) {
                    if (dayCounter == 5) {
                        break;
                    } else {
                        tmpList = new ArrayList<>();
                        mealsGroupedByDays.put(dayList.get(dayCounter), tmpList);
                        dayCounter++;
                    }
                } else {
                    if (tmpList != null)
                        tmpList.add(e.text());
                }
            }
        }
        return mealsGroupedByDays;
    }

}
