package com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants.implementations;

import com.github.hannesknutsson.hungryboii.api.dataclasses.Dish;
import com.github.hannesknutsson.hungryboii.api.dataclasses.OpenHours;
import com.github.hannesknutsson.hungryboii.api.dataclasses.Time;
import com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants.abstractions.SimpleRestaurant;
import com.github.hannesknutsson.hungryboii.api.exceptions.OCRException;
import com.github.hannesknutsson.hungryboii.api.exceptions.ParsingOutdated;
import com.github.hannesknutsson.hungryboii.api.exceptions.TotallyBrokenDudeException;
import com.github.hannesknutsson.hungryboii.api.exceptions.WebPageBroken;
import com.github.hannesknutsson.hungryboii.api.statichelpers.HttpHelper;
import com.github.hannesknutsson.hungryboii.api.statichelpers.OpticalCharacterRecognitionHelper;
import com.github.hannesknutsson.hungryboii.api.statichelpers.TimeHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus.*;

public class VidaArena extends SimpleRestaurant {

    public VidaArena() {
        super("Vida Arena", 109, new OpenHours(new Time(11, 30), new Time(14, 0)));
    }

    @Override
    public void refreshData() {
        try {
            //Retrieve image URL
            Document webPage = HttpHelper.getWebPage("https://www.vaxjolakers.se/mat-dryck/lunchmeny");
            List<Element> elementList = filterWebPage(webPage, "body > div > div > div > div > div > div > div > div > img");
                ///html/body/div[4]/div[2]/div/div[2]/div[2]/div[1]/div/div/img
            //Retrieve image
            String imageSource = elementList.get(1).getAllElements().get(0).attributes().asList().get(1).toString().replace("\"", "").substring(4);
            URL url;
            BufferedImage image;
            try {
                url = new URL(imageSource);
                image = ImageIO.read(url);
            } catch (IOException e) {
                throw new WebPageBroken(e);
            }

            //Parse image
            Rectangle areaToCapture;
            switch(TimeHelper.getDayOfWeek()) {
                case MONDAY:
                    areaToCapture = new Rectangle(465, 105,950, 130);
                    break;
                case TUESDAY:
                    areaToCapture = new Rectangle(465, 285,1395, 135);
                    break;
                case WEDNESDAY:
                    areaToCapture = new Rectangle(465, 460,1395, 135);
                    break;
                case THURSDAY:
                    areaToCapture = new Rectangle(465, 645,1395, 135);
                    break;
                case FRIDAY:
                    areaToCapture = new Rectangle(465, 815,1365, 195);
                    break;
                default:
                    throw new TotallyBrokenDudeException();
            }

            List<String> resultlist;
            try {
                resultlist = OpticalCharacterRecognitionHelper.parseImageArea(image, areaToCapture);
            } catch (OCRException e) {
                throw new ParsingOutdated("Failed parsing image");
            }

            resultlist = resultlist.stream().filter(str -> !str.isEmpty()).collect(Collectors.toList());

            List<Dish> availableDishesNonSynchronous = resultlist.stream().map(Dish::new).collect(Collectors.toList());

            availableDishes.clear();
            availableDishes.addAll(availableDishesNonSynchronous);

            status = OK;
        } catch (WebPageBroken exception) {
            status = WEBSITE_BROKEN;
        } catch (TotallyBrokenDudeException weekend) {
            status = WEEKEND;
        } catch (ParsingOutdated ocrBroken) {
            status = PARSING_BROKEN;
        }
    }

    private List<Element> filterWebPage(Document toFilter, String filterQuery) {
        return toFilter.select(filterQuery);
    }
}
