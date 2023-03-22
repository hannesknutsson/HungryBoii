package com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants;

import com.github.hannesknutsson.hungryboii.api.dataclasses.Dish;
import com.github.hannesknutsson.hungryboii.api.dataclasses.OpenHours;
import com.github.hannesknutsson.hungryboii.api.dataclasses.Time;
import com.github.hannesknutsson.hungryboii.api.exceptions.TotallyBrokenDudeException;
import com.github.hannesknutsson.hungryboii.api.exceptions.WebPageBroken;
import com.github.hannesknutsson.hungryboii.api.statichelpers.HttpHelper;
import com.github.hannesknutsson.hungryboii.api.statichelpers.TimeHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus.*;

public class VidaArena extends SimpleRestaurant {

    private static final Logger LOG = LoggerFactory.getLogger(VidaArena.class);
    private static final String targetUrl = "https://www.vaxjolakers.se/mat-dryck/lunchmeny";

    public VidaArena() {
        super("Vida Arena", targetUrl, 115, new OpenHours(new Time(11, 30), new Time(14, 0)));
    }

    @Override
    public void refreshData() {
        try {
            //Retrieve image URL
            Document webPage = HttpHelper.getWebPage("https://www.vaxjolakers.se/mat-dryck/lunchmeny");
            List<Element> elementList = filterWebPage(webPage, "body > div > div > div > div > div > div > div > div > img");

            //Retrieve image
            String imageSource = elementList.get(1).getAllElements().get(0).attributes().asList().get(1).toString().replace("\"", "").substring(4);
            URL url;
            BufferedImage image;
            try {
                url = new URL(imageSource);
                image = ImageIO.read(url);
            } catch (IOException e) {
                LOG.warn(e.toString());
                throw new WebPageBroken(e);
            }

            //Parse image
            Rectangle areaToCapture = switch (TimeHelper.getDayOfWeek()) {
                case MONDAY -> new Rectangle(387, 105, 800, 115);
                case TUESDAY -> new Rectangle(387, 250, 800, 115);
                case WEDNESDAY -> new Rectangle(387, 400, 800, 115);
                case THURSDAY -> new Rectangle(387, 550, 800, 115);
                case FRIDAY -> new Rectangle(387, 695, 800, 115);
                default -> throw new TotallyBrokenDudeException();
            };

            List<String> resultlist = new ArrayList<>();


            resultlist = resultlist.stream().filter(str -> !str.isEmpty()).collect(Collectors.toList());

            List<Dish> availableDishesNonSynchronous = resultlist.stream().map(Dish::new).collect(Collectors.toList());

            availableDishes.clear();
            availableDishes.addAll(availableDishesNonSynchronous);

            status = OK;
        } catch (WebPageBroken exception) {
            LOG.warn(exception.toString());
            status = WEBSITE_BROKEN;
        } catch (TotallyBrokenDudeException weekend) {
            LOG.warn(weekend.toString());
            status = WEEKEND;
        } catch (Exception e) {
            LOG.warn(e.toString());
        }
    }

    private List<Element> filterWebPage(Document toFilter, String filterQuery) {
        return toFilter.select(filterQuery);
    }
}
