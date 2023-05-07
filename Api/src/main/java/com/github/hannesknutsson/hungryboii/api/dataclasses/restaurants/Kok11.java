package com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants;

import com.github.hannesknutsson.hungryboii.api.dataclasses.Dish;
import com.github.hannesknutsson.hungryboii.api.dataclasses.OpenHours;
import com.github.hannesknutsson.hungryboii.api.dataclasses.Time;
import com.github.hannesknutsson.hungryboii.api.exceptions.ParsingOutdated;
import com.github.hannesknutsson.hungryboii.api.exceptions.WebPageBroken;
import com.github.hannesknutsson.hungryboii.api.statichelpers.HttpHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus.*;

public class Kok11 extends SimpleRestaurant {

    private static final Logger LOG = LoggerFactory.getLogger(Kok11.class);

    private static final String targetUrl = "https://www.kok11.se/dagens-lunch/";
    private static final String filterQuery = "div > div > div > div > div > div > ul > li";

    public Kok11() {
        super("Kök 11", targetUrl, new OpenHours(new Time(11, 30), new Time(13, 30)));
    }

    @Override
    public void refreshData() throws ParsingOutdated, WebPageBroken {
            Document webPage = HttpHelper.getWebPage(targetUrl);
            List<Element> elementlist = webPage.select(filterQuery);
            List<String> alternatives = elementlist.stream().flatMap(e -> e.childNodesCopy().stream()).map(Node::toString).collect(Collectors.toList());
            if (!alternatives.isEmpty()) {
                alternatives.remove(0);
            }
            List<Dish> dishes = alternatives.stream().map(Dish::new).toList();

            if (dishes.isEmpty()) {
                throw new ParsingOutdated("The number of dishes parsed was 0");
            }

            availableDishes.addAll(dishes);
    }
}
