package com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants;

import com.github.hannesknutsson.hungryboii.api.dataclasses.Dish;
import com.github.hannesknutsson.hungryboii.api.dataclasses.OpenHours;
import com.github.hannesknutsson.hungryboii.api.dataclasses.Time;
import com.github.hannesknutsson.hungryboii.api.enumerations.Weekday;
import com.github.hannesknutsson.hungryboii.api.exceptions.ParsingOutdated;
import com.github.hannesknutsson.hungryboii.api.exceptions.WebPageBroken;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus.*;
import static com.github.hannesknutsson.hungryboii.api.statichelpers.TimeHelper.getDayOfWeek;

public class VidaArena extends SimpleRestaurant {

    private static final Logger LOG = LoggerFactory.getLogger(VidaArena.class);
    private static final String targetUrl = "https://www.vaxjolakers.se/api/layouts/site-layouts/";

    public VidaArena() {
        super("Vida Arena", targetUrl, 115, new OpenHours(new Time(11, 30), new Time(14, 0)));
    }

    @Override
    public void refreshData() {
        try {
            var url = new URL(targetUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            JSONObject jo = new JSONObject("{\"d\":" + content + "}");
            List<String> jsonMenu = jo.getJSONArray("d").toList()
                    .stream()
                    .map(HashMap.class::cast)
                    .filter(map -> ((String)map.get("name")).endsWith("Lunch"))
                    .map(obj -> obj.get("siteSections"))
                    .map(ArrayList.class::cast)
                    .map(arr -> arr.get(arr.size() - 1))
                    .map(HashMap.class::cast)
                    .map(obj -> obj.get("config"))
                    .map(HashMap.class::cast)
                    .map(obj -> obj.get("body"))
                    .map(obj -> (ArrayList<HashMap>)obj)
                    .map(arr -> arr.stream()
                            .map(obj -> obj.get("data"))
                            .map(HashMap.class::cast)
                            .map(obj -> obj.get("text"))
                            .map(String.class::cast)
                            .toList()
                    )
                    .findFirst()
                    .orElse(List.of());

            Map<Weekday, List<String>> mealsGroupedByDays = parseElementsToMealMap(jsonMenu);
            List<String> todaysAlternatives = mealsGroupedByDays.get(getDayOfWeek());
            availableDishes.clear();

            if (todaysAlternatives.isEmpty()) {
                throw new ParsingOutdated();
            }

            for (String alternative : todaysAlternatives) {
                availableDishes.add(new Dish(alternative));
            }

            status = OK;
        } catch (WebPageBroken exception) {
            status = WEBSITE_BROKEN;
        } catch (ParsingOutdated parsingOutdated) {
            status = PARSING_BROKEN;
        } catch (IOException e) {
            LOG.warn(e.toString());
        }
    }

    private Map<Weekday, List<String>> parseElementsToMealMap(List<String> elementList) {
        Map<Weekday, List<String>> mealsGroupedByDays = new HashMap<>();
        List<String> weekdays = Arrays.asList("Måndag", "Tisdag", "Onsdag", "Torsdag", "Fredag");
        AtomicReference<Weekday> currentWeekDay = new AtomicReference<>(Weekday.NOT_A_WEEKDAY);

        elementList.stream()
                .filter(day -> !day.isEmpty())
                .forEach(meal -> {
                    Optional<Weekday> isWeekday = weekdays.stream()
                            .filter(meal::contains)
                            .map(weekday -> Weekday.values()[weekdays.indexOf(weekday)])
                            .findFirst();

                    if (isWeekday.isPresent()) {
                        mealsGroupedByDays.put(isWeekday.get(), new ArrayList<>());
                        currentWeekDay.set(isWeekday.get());
                    } else {
                        mealsGroupedByDays.get(currentWeekDay.get()).add(meal);
                    }
                });

        return mealsGroupedByDays;
    }
}
