package com.github.hannesknutsson.hungryboii.discord.utilities.statichelpers;

import com.github.hannesknutsson.hungryboii.discord.exceptions.WebPageBroken;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class HttpHelper {

    public static Document getWebPage(String targetUrl) throws WebPageBroken {
        try {
            return Jsoup.connect(targetUrl).followRedirects(false).timeout(6000).get();
        } catch (IOException e) {
            throw new WebPageBroken(e);
        }
    }
}

