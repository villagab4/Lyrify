package com.gabevillasana.lyrify;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gabe Villasana on 5/21/2017.
 */

public class SearchActivity extends ListActivity {

    private Map<String, Integer> songs;
    private String query;
    private WebScraper scraper;

    public SearchActivity() {
        // Map to store song options and their frequencies
        songs = new HashMap<>();
        query = "";
        scraper = new WebScraper();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ListView list = (ListView) findViewById(R.id.list);

        System.out.println("GOT HERE");

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            this.query = intent.getStringExtra(SearchManager.QUERY);
            this.query = this.modifyQuery();
            System.out.println("THE QUERY IS: " + this.query);
            this.geniusSearch();
            //this.metroSearch();
            //this.azSearch();
        }
    }

    private String modifyQuery() {
        //remove spaces at front
        while (query.charAt(0) == ' ') {
            query = query.substring(1);
        }
        //removes spaces at end
        while (query.charAt(query.length() - 1) == ' ') {
            query = query.substring(0, query.length() - 1);
        }
        int count = 0;
        String mQuery = "";
        //changes space characters to +
        while (count < query.length()) {
            if (query.charAt(count) == ' ') {
                //replace space with +
                mQuery += "+";
            } else mQuery += query.charAt(count);
            count++;
        }
        return mQuery;
    }

    private void metroSearch() {
        String metroURL = "http://www.google.com/#q=" + this.query + "+site:metrolyrics.com";
        // Open HTML page
        try {
            this.parseWebpage(metroURL);
        } catch (IOException e) {
            System.out.println("Failed to parse");
        }
    }

    private void azSearch() {
        String azURL = "http://www.google.com/#q=" + this.query + "+site:azlyrics.com";
        // Open HTML page
        try {
            this.parseWebpage(azURL);
        } catch (IOException e) {
            System.out.println("Failed to parse");
        }
    }


    private void geniusSearch() {
        String geniusURL = "http://www.google.com/#q=" + this.query + "+site:genius.com";
        // Open HTML page
        try {
            this.parseWebpage(geniusURL);
        } catch (IOException e) {
            System.out.println("Failed to parse");
        }
    }

    private void parseWebpage(String url) throws IOException {
        System.out.println("URL TO SEARCH: " + url);
        this.scraper.execute(url); //asynchronous task automatically populates the map
    }

    private class WebScraper extends AsyncTask<String, Void, String> {

        public WebScraper() {}

        @Override
        protected String doInBackground(String... urls) {
            Document doc;
            try {
                doc = Jsoup.connect(urls[0]).get();
                System.out.println("DOCS IS CREATED");
            } catch (IOException e) {
                System.out.println("Failed to open document");
                return "";
            }
            Elements results = doc.getElementsByClass("rc");
            int coun = 0;
            for (Element lmnt : results) {
                System.out.println(coun++);
                System.out.println(lmnt.text());
            }
            System.out.println("Count is : " + coun);
            String key = "test";
            //noinspection Since15
            SearchActivity.this.songs.put(key, SearchActivity.this.songs.getOrDefault(key, 0) + 1);
            // return requested
            return "";
        }

    }

}
