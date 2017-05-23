package com.gabevillasana.lyrify;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    private static String API_KEY = "AIzaSyCHi4WuRfV6H2KeaEfvhoT7ZzX3cgUQ7xE";
    private static String SEARCH_ENGINE_ID = "000956957799242292465:llxeq3fiu8w";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ListView list = (ListView) findViewById(R.id.list);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            this.query = intent.getStringExtra(SearchManager.QUERY);
            this.query = this.modifyQuery(); // put in correct CSE format
            this.scraper.execute(query);
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
        mQuery = "https://www.googleapis.com/customsearch/v1?key=" + API_KEY + "&cx="
                + SEARCH_ENGINE_ID + "&fields=items/title" + "&q=" + mQuery;
        return mQuery;
    }

    private class WebScraper extends AsyncTask<String, Void, String> {

        public WebScraper() {}

        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(urls[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                System.out.println("HERE");
                BufferedReader read = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                // TODO: Update to correclty parse JSON
                String me;
                while ((me = read.readLine()) != null) {
                    System.out.println(me);
                }
                conn.disconnect();
            } catch (IOException e) {
                Log.d("Error", e.getMessage());
                return "";
            }
            /**
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
             */
            String key = "test";
            //noinspection Since15
            SearchActivity.this.songs.put(key, SearchActivity.this.songs.getOrDefault(key, 0) + 1);
            // return requested
            return "";
        }

    }

}
