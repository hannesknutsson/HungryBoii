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
import com.github.hannesknutsson.hungryboii.api.statichelpers.TimeHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus.*;
import static com.github.hannesknutsson.hungryboii.api.enumerations.Weekday.NOT_A_WEEKDAY;
import static com.github.hannesknutsson.hungryboii.api.statichelpers.TimeHelper.getDayOfWeek;

public class Ostergatan extends SimpleRestaurant {

    private static final String targetUrl = "https://ostergatansrestaurang.se/lunch/";

    public Ostergatan() {
        super("Östergatans restaurang", targetUrl, 99, new OpenHours(new Time(11, 00), new Time(13, 30)));
    }

    @Override
    public void refreshData() {
        try {
            Document webPage = HttpHelper.getWebPage(targetUrl);
            //List<Element> elementList = filterWebPage(webPage, "div#main-content > article > div > div > div > div > div > div > div > div > div > h4 > p > i");
            List<Element> elementList = filterWebPage(webPage, "div#main-content > article > div > div > div > div > div > div > div > div > div > div > p > *");
            //List<String> filtereElements = elementList.stream().flatMap(e -> e.childNodesCopy().stream()).map(Node::toString).collect(Collectors.toList());
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
        } catch (ParsingOutdated | TotallyBrokenDudeException parsingOutdated) {
            status = PARSING_BROKEN;
        }
    }

    private List<Element> filterWebPage(Document toFilter, String filterQuery) {
        return toFilter.select(filterQuery);
    }

    private Map<Weekday, List<String>> parseElementsToMealMap(List<Element> elementList) throws ParsingOutdated {
        Map<Weekday, List<String>> mealsGroupedByDays = new HashMap<>();

        List<TextNode> textNodes = new ArrayList<>(elementList)
                .stream()
                .flatMap(element -> element.childNodesCopy().stream())
                .collect(Collectors.toList())
                .stream()
                .filter(node -> node instanceof TextNode)
                .map(node -> (TextNode) node)
                .collect(Collectors.toList());

        if (textNodes.size() != 11) {
            throw new ParsingOutdated("Unexected number of dishes");
        }

        for (int i = 0; i < 10 ; i += 2) {
            List<String> meals = List.of(textNodes.get(i).text(), textNodes.get(i+1).text(), textNodes.get(10).text());
            mealsGroupedByDays.put(Weekday.values()[i / 2], meals);
        }

        return mealsGroupedByDays;
    }
}
