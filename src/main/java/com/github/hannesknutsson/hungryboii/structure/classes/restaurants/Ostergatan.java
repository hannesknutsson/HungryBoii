package com.github.hannesknutsson.hungryboii.structure.classes.restaurants;

import com.github.hannesknutsson.hungryboii.structure.classes.Dish;
import com.github.hannesknutsson.hungryboii.structure.enumerations.Weekday;
import com.github.hannesknutsson.hungryboii.structure.exceptions.ParsingOutdated;
import com.github.hannesknutsson.hungryboii.structure.exceptions.TotallyBrokenDudeException;
import com.github.hannesknutsson.hungryboii.structure.exceptions.WebPageBroken;
import com.github.hannesknutsson.hungryboii.structure.templates.SimpleRestaurant;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.HttpHelper;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.TimeHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.hannesknutsson.hungryboii.structure.enumerations.RestaurantStatus.*;
import static com.github.hannesknutsson.hungryboii.structure.enumerations.Weekday.NOT_A_WEEKDAY;
import static com.github.hannesknutsson.hungryboii.utilities.statichelpers.TimeHelper.getDayOfWeek;

public class Ostergatan extends SimpleRestaurant {

    private static final String targetUrl = "https://www.ostergatansrestaurang.se/";

    public Ostergatan() {
        super("Östergatans restaurang");
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
