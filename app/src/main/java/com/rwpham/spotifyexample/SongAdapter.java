package com.rwpham.spotifyexample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackSimple;

public class SongAdapter extends
        RecyclerView.Adapter<SongAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView artist;
        public TextView title;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            artist = (TextView) itemView.findViewById(R.id.artist);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
    // ... view holder defined above...

    // Store a member variable for the contacts
    private List<TrackSimple> songList;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public SongAdapter(Context context, List<TrackSimple> songs) {
        songList = songs;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.row_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SongAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        String artist = songList.get(position).artists.get(0).name;
        String title = songList.get(position).name;

        System.out.printf("onbindviewholder: artist: %s title: %s \n", artist, title);
        // Set item views based on your views and data model
        TextView artistTextView = viewHolder.artist;
        artistTextView.setText(artist);
        TextView titleTextView = viewHolder.artist;
        titleTextView.setText(title);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

}