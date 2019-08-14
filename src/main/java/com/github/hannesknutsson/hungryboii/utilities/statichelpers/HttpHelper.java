package com.github.hannesknutsson.hungryboii.utilities.statichelpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class HttpHelper {

    public static Document getDocumentPage(String targetUrl) throws IOException, ParserConfigurationException, SAXException {
        return Jsoup.connect(targetUrl).followRedirects(false).timeout(6000).get();
    }
}
