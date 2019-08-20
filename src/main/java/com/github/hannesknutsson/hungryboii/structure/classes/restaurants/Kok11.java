package com.github.hannesknutsson.hungryboii.structure.classes.restaurants;

import com.github.hannesknutsson.hungryboii.structure.classes.Dish;
import com.github.hannesknutsson.hungryboii.structure.exceptions.ParsingOutdated;
import com.github.hannesknutsson.hungryboii.structure.exceptions.WebPageBroken;
import com.github.hannesknutsson.hungryboii.structure.templates.SimpleRestaurant;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.HttpHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.github.hannesknutsson.hungryboii.structure.enumerations.RestaurantStatus.*;

public class Kok11 extends SimpleRestaurant {

    private static Logger LOG = LoggerFactory.getLogger(Kok11.class);

    private static final String targetUrl = "http://www.kok11.se/dagenslunch-vaxjo";
    private static final String filterQuery = "div > div > div > div > div > h2";

    public Kok11() {
        super("Kök 11");
    }

    @Override
    public CopyOnWriteArrayList<Dish> getTodaysDishes() {
        return availableDishes;
    }

    @Override
    public void refreshData() {
        try {
            Document webPage = HttpHelper.getWebPage(targetUrl);
            List<Element> elementlist = webPage.select(filterQuery);
            List<String> alternatives = elementlist.stream().flatMap(e -> e.childNodesCopy().stream()).map(Node::toString).collect(Collectors.toList());
            List<Dish> dishes = alternatives.stream().map(Dish::new).collect(Collectors.toList());

            availableDishes.clear();

            if (dishes.size() <= 0) {
                throw new ParsingOutdated();
            }

            availableDishes.addAll(dishes);
            status = OK;

        } catch (WebPageBroken exception) {
            status = WEBSITE_BROKEN;
            LOG.error("Failed to refresh menu. Kök11's WEBSITE seems to be broken..");
        } catch (ParsingOutdated parsingOutdated) {
            status = PARSING_BROKEN;
            LOG.error("Failed to refresh menu. The PARSING of Kök11's website seems to be broken..");
        }
    }
}
