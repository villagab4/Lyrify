package com.gabevillasana.lyrify;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Gabe Villasana on 5/21/2017.
 */

public class SearchActivity extends ListActivity {

    private Map<String, Integer> songs;
    private String query;

    public SearchActivity() {
        // TreeMap to store song options
        songs = new TreeMap<String, Integer>();
        query = "";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            this.query = intent.getStringExtra(SearchManager.QUERY);
            this.query = this.modifyQuery();
            this.geniusSearch();
            this.metroSearch();
            this.azSearch();
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
        String metroURL = "http://www.google.com/#q=" + query + "+site:metrolyrics.com";
        // Open HTML page
        this.parseWebpage(metroURL);
    }

    private void azSearch() {
        String azURL = "http://www.google.com/#q=" + query + "+site:azlyrics.com";
        // Open HTML page
        this.parseWebpage(azURL);
    }


    private void geniusSearch() {
        String geniusURL = "http://www.google.com/#q=" + query + "+site:genius.com";
        // Open HTML page
        this.parseWebpage(geniusURL);
    }

    private void parseWebpage(String url) {
        // TODO: PARSE WEBPAGE USING HTML PARSER
    }
}
