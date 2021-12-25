package com.github.hannesknutsson.hungryboii.discord.utilities.statichelpers.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class ListMenuHelper {

    public static void getMenus(EmbedBuilder embedObject) {
        try {
            String url = "http://localhost:8080/api/hungryboii/xml";
            Document webPage = Jsoup.connect(url).ignoreContentType(true).followRedirects(false).timeout(6000).get();
            compileMenus(embedObject, webPage);
        } catch (IOException webPageBroken) {
            webPageBroken.printStackTrace();
            embedObject.addField(new MessageEmbed.Field("Error", "Something went wrong when fetching the menus", false));
        }
    }

    private static void compileMenus(EmbedBuilder embedObject, Document webPage) {
        List<Element> elements = webPage.select("restaurant");
        for (Element e : elements) {
            Elements restaurant = e.select("restaurant");
            String name = restaurant.select("name").text();
            Elements error = e.select("error");
            String text;
            if (error.size() > 0) {
                text = error.text();
            } else {
                text = formatSingleMenu(e);
            }
            embedObject.addField(new MessageEmbed.Field(name, text, false));
        }
    }

    private static String formatSingleMenu(Element e) {
        StringBuilder menu = new StringBuilder();
        String info = e.select("info").text();
        menu.append(info).append("\n");
        Elements entries = e.select("entry");
        for (Element entry : entries) {
            menu.append("\t• ").append(entry.text()).append("\n");
        }
        return menu.toString();
    }
}
