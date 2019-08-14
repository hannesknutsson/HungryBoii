package com.github.hannesknutsson.hungryboii.structure.classes.restaurants;

import com.github.hannesknutsson.hungryboii.structure.classes.Dish;
import com.github.hannesknutsson.hungryboii.structure.exceptions.CouldNotRefreshException;
import com.github.hannesknutsson.hungryboii.structure.templates.Restaurant;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.HttpHelper;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.github.hannesknutsson.hungryboii.utilities.statichelpers.TimeHelper.getDayOfWeek;

public class Ostergatan implements Restaurant {

    private static final String name = "Östergatans restaurang";
    private static final String targetUrl = "https://www.ostergatansrestaurang.se/";

    CopyOnWriteArrayList<Dish> availableDishes;

    public Ostergatan() {
        availableDishes = new CopyOnWriteArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CopyOnWriteArrayList<Dish> getTodaysDishes() {
        return availableDishes;
    }

    @Override
    public void refreshData() throws CouldNotRefreshException {

        List<Element> elementlist = null;
        try {
            elementlist = HttpHelper.getDocumentPage(targetUrl).select("body > div > div > div > div > p:eq(8)");
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new CouldNotRefreshException("Östergatans restaurang failed to refresh!", e);
        }

        List<TextNode> textNodes = new ArrayList<>(elementlist)
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

        Map<Integer, List<String>> mealsSortedByDays = new HashMap<>();

        List<String> tmpList = null;
        for (TextNode node : textNodes) {
            String nodeString = node.text();
            if (nodeString.length() < 8) {
                int day;
                switch (nodeString.toLowerCase()) {
                    case "måndag":
                        day = 0;
                        break;
                    case "tisdag":
                        day = 1;
                        break;
                    case "onsdag":
                        day = 2;
                        break;
                    case "torsdag":
                        day = 3;
                        break;
                    case "fredag":
                        day = 4;
                        break;
                    default:
                        day = -1;
                }
                if (day >= 0 && day < 5) {
                    tmpList = new ArrayList<>();
                    mealsSortedByDays.put(day, tmpList);
                }
            } else {
                if (tmpList != null) {
                    tmpList.add(nodeString);
                }
            }
        }

        List<String> todaysAlternatives = mealsSortedByDays.get(getDayOfWeek());
        availableDishes.clear();

        if (todaysAlternatives != null) {
            for (String alternative : todaysAlternatives) {
                availableDishes.add(new Dish(alternative));
            }
        }
    }
}
