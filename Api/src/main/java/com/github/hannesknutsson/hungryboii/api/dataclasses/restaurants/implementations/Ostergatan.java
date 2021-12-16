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

    private static final String targetUrl = "https://www.ostergatansrestaurang.se/";

    public Ostergatan() {
        super("Östergatans restaurang", 95, new OpenHours(new Time(11, 30), new Time(14, 0)));
    }

    @Override
    public void refreshData() {
        try {
            Document webPage = HttpHelper.getWebPage(targetUrl);
            List<Element> elementList = filterWebPage(webPage, "body > div > div > div > div > p:eq(8)");
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

        textNodes.remove(0);
        textNodes.remove(textNodes.size() - 1);
        textNodes.remove(textNodes.size() - 1);
        textNodes.remove(textNodes.size() - 1);
        textNodes.remove(textNodes.size() - 1);

        List<String> tmpList = null;
        for (TextNode node : textNodes) {
            String nodeString = node.text();
            Weekday attemptToParsedDay = TimeHelper.parseStringToWeekday(nodeString);
            if (attemptToParsedDay == NOT_A_WEEKDAY) {
                if (tmpList != null) {
                    tmpList.add(nodeString);
                }
            } else {
                tmpList = new ArrayList<>();
                mealsGroupedByDays.put(attemptToParsedDay, tmpList);
            }
        }
        return mealsGroupedByDays;
    }
}
