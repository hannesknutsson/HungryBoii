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
import org.jsoup.nodes.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus.*;
import static com.github.hannesknutsson.hungryboii.api.statichelpers.TimeHelper.getDayOfWeek;

public class Ostergatan extends SimpleRestaurant {

    private static final String targetUrl = "https://ostergatansrestaurang.se/lunch/";

    public Ostergatan() {
        super("Östergatans restaurang", targetUrl, 110, new OpenHours(new Time(11, 0), new Time(13, 30)));
    }

    @Override
    public void refreshData() {
        try {
            Document webPage = HttpHelper.getWebPage(targetUrl);
            List<Element> elementList = filterWebPage(webPage, "div#main-content > article > div > div > div > div > div > div > div > div > div > div");
            Map<Weekday, List<String>> mealsGroupedByDays = parseElementsToMealMap(elementList);
            List<String> todaysAlternatives = mealsGroupedByDays.get(getDayOfWeek());
            availableDishes.clear();

            if (mealsGroupedByDays.size() != 5 && todaysAlternatives.size() <= 0) {
                throw new ParsingOutdated();
            }

            if (todaysAlternatives != null) {
                for (String alternative : todaysAlternatives) {
                    availableDishes.add(new Dish(alternative));
                }
            }

            status = OK;
        } catch (WebPageBroken exception) {
            status = WEBSITE_BROKEN;
        } catch (ParsingOutdated parsingOutdated) {
            status = PARSING_BROKEN;
        }
    }

    private List<Element> filterWebPage(Document toFilter, String filterQuery) {
        return toFilter.select(filterQuery);
    }

    private Map<Weekday, List<String>> parseElementsToMealMap(List<Element> elementList) throws ParsingOutdated {
        Map<Weekday, List<String>> mealsGroupedByDays = new HashMap<>();

        var nodesGroupedByWeekday = elementList.stream().map(Node::childNodesCopy).toList();

        if (nodesGroupedByWeekday.size() < 6) {
            throw new ParsingOutdated("Did not find expected weekdays");
        }

        var vegAlt = nodesGroupedByWeekday.get(5).stream()
                .filter(node -> node instanceof Element)
                .filter(element -> !((Element) element).text().isBlank())
                .map(element -> ((Element) element).text())
                .toList();

        for (int i = 0; i < 5; i++) {
            List<String> meals = nodesGroupedByWeekday.get(i).stream()
                    .filter(node -> node instanceof Element)
                    .filter(element -> !((Element) element).text().isBlank())
                    .map(element -> ((Element) element).text())
                    .collect(Collectors.toList());
            var weekday = Weekday.values()[i];
            meals.addAll(vegAlt);
            mealsGroupedByDays.put(weekday, meals);
        }

        return mealsGroupedByDays;
    }
}
