package com.rwpham.spotifyexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import java.util.Random;

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

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.TrackSimple;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.R.attr.max;
import static android.R.id.list;

public class MainActivity extends Activity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback
{

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "f54c75abaec74128b69f9de4fb5a7b31";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "spotifyalarm://callback";

    private Player mPlayer;
    private SongAdapter mAdapter;

    SpotifyApi wrapper;
    SpotifyService spotify;
    RecyclerView recyclerView;
    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);


        wrapper = new SpotifyApi();
        spotify = wrapper.getService();




        recyclerView = (RecyclerView) findViewById(R.id.search_results);
        Context context = getApplicationContext();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                wrapper.setAccessToken(response.getAccessToken());
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
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");

        spotify.getAlbum("1lXY618HWkwYKJWBRYR4MK", new Callback<Album>() {
            @Override
            public void success(Album album, Response response) {
                Log.d("Album success", album.name);
                Pager<TrackSimple> pager =  album.tracks;
                List<TrackSimple> list = pager.items;

                //Not sure if this is adding the entire more life list to it
                mAdapter = new SongAdapter(getApplicationContext(), list);

                recyclerView.setAdapter(mAdapter);

                Random r = new Random();
                int rand = r.nextInt(10 - 1) + 1;

                for(int i = 0; i < list.size(); i++) {
                    System.out.println("TrackSimple: " + list.get(i).name);
                }
                System.out.printf("I'm playing: %s\n", list.get(rand).name);
                mPlayer.playUri(null, list.get(rand).uri, 0, 0);

            }
            @Override
            public void failure(RetrofitError error) {
                Log.d("Album failure", error.toString());
            }
        });
        //mPlayer.playUri(null, "spotify:user:125759705:playlist:3XTblfwdf0KLInAYUhq6Hn", 0, 0);
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error e) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }
}