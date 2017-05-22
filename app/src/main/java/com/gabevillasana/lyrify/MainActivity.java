package com.gabevillasana.lyrify;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

public class MainActivity extends Activity implements SpotifyPlayer.NotificationCallback, ConnectionStateCallback, SearchView.OnQueryTextListener {

    // Client ID
    private static final String CLIENT_ID = "a3fb3dfeb5354d8e9c90ad82896f16a2";

    // Custom Redirect URI
    private static final String REDIRECT_URI = "lyrify://callback";

    // Request code that will be used to verify if the result comes from correct activity
    private static final int REQUEST_CODE = 313689;

    private Player mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        // Sets the list of scopes that user needs to grant
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        builder.setShowDialog(true);
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        SearchView searchView = (SearchView) findViewById(R.id.search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setQueryHint("Search song title, lyric, or artist");
        searchView.setOnQueryTextListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        //Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            // Extract OAuth access token
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                // Pass token to config object to create player
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addNotificationCallback(MainActivity.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        // Destroy player to avoid leaking resources
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String s) {
        Log.d("MainActivity", "Received connection message: " + s);
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            //Handle even type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            //Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent searchIntent = new Intent(this, SearchActivity.class);
        searchIntent.putExtra(SearchManager.QUERY, query);
        searchIntent.setAction(Intent.ACTION_SEARCH);
        startActivity(searchIntent);
        return true; // we start the search activity manually
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
