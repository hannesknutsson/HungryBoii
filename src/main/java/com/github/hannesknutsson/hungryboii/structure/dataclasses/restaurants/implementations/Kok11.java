package com.github.hannesknutsson.hungryboii.structure.dataclasses.restaurants.implementations;

import com.github.hannesknutsson.hungryboii.structure.dataclasses.Dish;
import com.github.hannesknutsson.hungryboii.structure.dataclasses.OpenHours;
import com.github.hannesknutsson.hungryboii.structure.dataclasses.Time;
import com.github.hannesknutsson.hungryboii.structure.dataclasses.restaurants.abstractions.SimpleRestaurant;
import com.github.hannesknutsson.hungryboii.exceptions.ParsingOutdated;
import com.github.hannesknutsson.hungryboii.exceptions.WebPageBroken;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.HttpHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.hannesknutsson.hungryboii.structure.enumerations.RestaurantStatus.*;

public class Kok11 extends SimpleRestaurant {

    private static final String targetUrl = "http://www.kok11.se/dagenslunch-vaxjo";
    private static final String filterQuery = "div > div > div > div > div > h2";

    public Kok11() {
        super("Kök 11", 105, new OpenHours(new Time(11, 30), new Time(14, 0)));
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
        } catch (ParsingOutdated parsingOutdated) {
            status = PARSING_BROKEN;
        }
    }
}
