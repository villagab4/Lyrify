//package com.gabevillasana.lyrify;

import android.os.AsyncTask;
import android.provider.DocumentsContract;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * WebScraper class to parse HTML files.
 * Intended for use on google search results
 * @author Gabe Villasana
 */
/**

public class WebScraper extends AsyncTask<String, Void, String> {

    public WebScraper() {}

    @Override
    protected String doInBackground(String... urls) {
        Document doc;
        try {
            doc = Jsoup.connect(urls[0]).get();
        } catch (IOException e) {
            System.out.println("Failed to open document");
            return "";
        }
        Elements results = doc.getElementsByClass("rc");
        for (Element lmnt : results) {
            System.out.println(lmnt.toString());
        }
        // return requested
        return "";
    }

}

 */
