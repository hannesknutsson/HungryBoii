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

import static com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus.OK;
import static com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus.PARSING_BROKEN;
import static com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus.WEBSITE_BROKEN;
import static com.github.hannesknutsson.hungryboii.api.statichelpers.TimeHelper.getDayOfWeek;

public class MKCatering extends SimpleRestaurant {

    private static final String targetUrl = "https://mkcatering.se/dagens.html";
    private static final String filterQuery = "body > div > div > div > div > div.dagens";

    public MKCatering() {
        super("MK Catering", targetUrl, 99, new OpenHours(new Time(11, 30), new Time(14, 0)));
    }

    @Override
    public void refreshData() {
        try {
            Document webPage = HttpHelper.getWebPage(targetUrl);
            List<Element> elementList = filterWebpage(webPage, filterQuery);
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
        } catch (ParsingOutdated parsingOutdated) {
            status = PARSING_BROKEN;
        }
    }

    private List<Element> filterWebpage(Document toFilter, String filterQuery) {
        return toFilter.select(filterQuery);
    }

    private Map<Weekday, List<String>> parseElementsToMealMap(List<Element> elementList) {
        Map<Weekday, List<String>> mealsGroupedByDays = new HashMap<>();

        for (Element e : elementList) {
            var meals = e.select("p")
                    .get(0)
                    .childNodesCopy()
                    .stream()
                    .map(node -> node.toString().trim().replaceAll("&amp;", "&"))
                    .filter(text -> !text.equals("<br>"))
                    .toList();
            var dayIndex = elementList.indexOf(e);
            mealsGroupedByDays.put(Weekday.values()[dayIndex], meals);
        }
        return mealsGroupedByDays;
    }
}
