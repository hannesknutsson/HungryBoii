package com.github.hannesknutsson.hungryboii.utilities.statichelpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class HttpHelper {

    public static Document getDocumentPage(String targetUrl) throws IOException {
        return Jsoup.connect(targetUrl).followRedirects(false).timeout(6000).get();
    }
}
