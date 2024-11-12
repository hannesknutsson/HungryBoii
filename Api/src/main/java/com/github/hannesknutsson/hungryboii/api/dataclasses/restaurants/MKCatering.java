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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.hannesknutsson.hungryboii.api.statichelpers.TimeHelper.getDayOfWeek;

public class MKCatering extends SimpleRestaurant {

    private static final String targetUrl = "https://www.mkcatering.se/#lunch";
    private static final String filterQuery = "div.elementor-image-box-content";

    public MKCatering() {
        super("MK Catering", targetUrl, new OpenHours(new Time(11, 30), new Time(14, 0)));
    }

    @Override
    public void refreshData() throws ParsingOutdated, WebPageBroken {
        Document webPage = HttpHelper.getWebPage(targetUrl);
        List<Element> elementList = filterWebpage(webPage, filterQuery);
        Map<Weekday, List<String>> mealsGroupedByDays = parseElementsToMealMap(elementList);
        List<String> todaysAlternatives = mealsGroupedByDays.get(getDayOfWeek());

        if (mealsGroupedByDays.size() != 5) {
            throw new ParsingOutdated("The number of retrieved days in the menu were not 5");
        }
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

        for (Element e : elementList.subList(0, 5)) {
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
