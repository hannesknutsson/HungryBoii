package com.github.hannesknutsson.hungryboii.structure.classes.restaurants;

import com.github.hannesknutsson.hungryboii.structure.classes.Dish;
import com.github.hannesknutsson.hungryboii.structure.enumerations.Weekday;
import com.github.hannesknutsson.hungryboii.structure.exceptions.ParsingOutdated;
import com.github.hannesknutsson.hungryboii.structure.exceptions.TotallyBrokenDudeException;
import com.github.hannesknutsson.hungryboii.structure.exceptions.WebPageBroken;
import com.github.hannesknutsson.hungryboii.structure.templates.SimpleRestaurant;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.HttpHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.github.hannesknutsson.hungryboii.structure.enumerations.RestaurantStatus.*;
import static com.github.hannesknutsson.hungryboii.utilities.statichelpers.TimeHelper.getDayOfWeek;

public class Futurum extends SimpleRestaurant {

    private static Logger LOG = LoggerFactory.getLogger(Futurum.class);

    private static final String targetUrl = "https://eurest.mashie.com/public/menu/restaurang+futurum/9ab27099?country=se";
    private static final String filterQuery = "div:eq(0) > div:eq(0) > span.day:eq(0), div > section.day-alternative > strong > span";

    public Futurum() {
        super("Futurum");
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
            LOG.error("Failed to refresh menu. Futurums WEBSITE seems to be broken..");
        } catch (ParsingOutdated | TotallyBrokenDudeException parsingOutdated) {
            status = PARSING_BROKEN;
            LOG.error("Failed to refresh menu. The PARSING of futurums website seems to be broken..");
        }
    }

    private List<Element> filterWebpage(Document toFilter, String filterQuery) throws WebPageBroken {
        return toFilter.select(filterQuery);
    }

    private Map<Weekday, List<String>> parseElementsToMealMap(List<Element> elementList) throws TotallyBrokenDudeException {
        Map<Weekday, List<String>> mealsGroupedByDays = new HashMap<>();
        List<String> tmpList = null;
        int dayCounter = 0;

        for (Element e : elementList) {
            if (!e.hasClass("container-week")) {
                if (e.hasClass("day")) {
                    if (dayCounter == 5) {
                        break;
                    } else {
                        tmpList = new ArrayList<>();
                        mealsGroupedByDays.put(getDayOfWeek(dayCounter), tmpList);
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
