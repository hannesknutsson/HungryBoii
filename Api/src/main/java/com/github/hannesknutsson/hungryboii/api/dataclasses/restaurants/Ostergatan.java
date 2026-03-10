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
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.hannesknutsson.hungryboii.api.statichelpers.TimeHelper.getDayOfWeek;

public class Ostergatan extends SimpleRestaurant {

    private static final Logger LOG = LoggerFactory.getLogger(Ostergatan.class);
    private static final String targetUrl = "https://ostergatansrestaurang.se/dagens-lunch";

    public Ostergatan() {
        super("Östergatans restaurang", targetUrl, new OpenHours(new Time(11, 0), new Time(13, 30)));
    }

    @Override
    public void refreshData() throws WebPageBroken, ParsingOutdated {
        Document webPage = HttpHelper.getWebPage(targetUrl);
        List<Element> elementList = filterWebPage(webPage, "article div.grid-container > div.flex-container");
        Map<Weekday, List<String>> mealsGroupedByDays = parseElementsToMealMap(elementList);
        List<String> todaysAlternatives = mealsGroupedByDays.get(getDayOfWeek());

        if (mealsGroupedByDays.size() != 5) {
            throw new ParsingOutdated("The number of retrieved days in the menu were not 5");
        }
        if (todaysAlternatives == null || todaysAlternatives.isEmpty()) {
            throw new ParsingOutdated("The number of retrieved dishes for today were less than 0");
        }

        for (String alternative : todaysAlternatives) {
            availableDishes.add(new Dish(alternative));
        }
    }

    private List<Element> filterWebPage(Document toFilter, String filterQuery) {
        return toFilter.select(filterQuery);
    }

    private Map<Weekday, List<String>> parseElementsToMealMap(List<Element> elementList) throws ParsingOutdated {
        Map<Weekday, List<String>> mealsGroupedByDays = new HashMap<>();

        // 5 weekdays + veggie/pasta of the week = 6
        if (elementList.size() < 6) {
            throw new ParsingOutdated("Did not find expected weekdays");
        }

        var vegAlt = extractDishes(elementList.get(5));

        for (int i = 0; i < 5; i++) {
            List<String> meals = extractDishes(elementList.get(i));
            var weekday = Weekday.values()[i];
            meals.addAll(vegAlt);
            mealsGroupedByDays.put(weekday, meals);
        }

        return mealsGroupedByDays;
    }

    private List<String> extractDishes(Element dayContainer) {
        List<String> dishes = new ArrayList<>();
        Elements paragraphs = dayContainer.select("div.textarea-block p");
        for (Element p : paragraphs) {
            Elements strongElements = p.select("strong");
            if (strongElements.isEmpty()) {
                String text = p.text().trim();
                if (!text.isBlank()) {
                    dishes.add(text);
                }
            } else {
                for (Element strong : strongElements) {
                    String text = strong.text().trim();
                    if (!text.isBlank()) {
                        dishes.add(text);
                    }
                }
            }
        }
        return dishes;
    }
}
