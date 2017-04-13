package com.rwpham.spotifyexample;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.TrackSimple;


public class Song {
    private String artist;
    private String title;
    private String uri;

    public Song(String artist, String title, String uri) {
        this.artist = artist;
        this.title = title;
        this.uri = uri;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getURI() {
        return uri;
    }

    private static int lastSongId = 0;

    public static ArrayList<Song> createSongsList(List<TrackSimple> trackList) {
        ArrayList<Song> Songs = new ArrayList<Song>();

        for (int i = 1; i <= trackList.size(); i++) {
            //only pulls the first artist from tracklist
            String artist = trackList.get(i).artists.get(0).name;
            String title = trackList.get(i).name;
            String uri = trackList.get(i).uri;
            Songs.add(new Song(artist, title, uri));
        }
        return Songs;
    }
}