package com.homidev.egypt.ehgezmal3ab;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by engineer on 27/03/18.
 * this class adapts Venue to use it in recyclerview
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class VenueItemAdapter extends RecyclerView.Adapter<VenueItemAdapter.VenueItemViewHolder>{

    private ConnectionManager connectionManager;
    private List<Venue> venueList;
    private Context context;

    public VenueItemAdapter(Context context) {
        this.context = context;
        connectionManager = ConnectionManager.getConnectionManager();
        venueList = connectionManager.getAllVenues();
    }

    //Called when RecyclerView needs a new VenueItemHolder to represent a venue item.
    @Override
    public VenueItemAdapter.VenueItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_venues_layout, parent, false);
        return new VenueItemViewHolder(view);
    }

    //Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(VenueItemAdapter.VenueItemViewHolder holder, int position) {
        Venue venue = venueList.get(position);
        //set the attributes of the venue item to be displayed
        holder.venueTitle.setText(venue.getVenueTitle());
        holder.venueStreet.setText(venue.getStreet());
        holder.venueArea.setText(venue.getArea());
        holder.venuePhoneNumber.setText(venue.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return venueList.size();
    }

    //A ViewHolder describes an item view and metadata about its place within the RecyclerView
    public class VenueItemViewHolder extends RecyclerView.ViewHolder {
        TextView venueTitle;
        TextView venueStreet;
        TextView venueArea;
        TextView venuePhoneNumber;

        public VenueItemViewHolder(View itemView) {
            super(itemView);
            venueTitle = (TextView) itemView.findViewById(R.id.venueTitle);
            venueStreet = (TextView) itemView.findViewById(R.id.venueStreet);
            venueArea = (TextView) itemView.findViewById(R.id.venueArea);
            venuePhoneNumber = (TextView) itemView.findViewById(R.id.venuePhoneNumber);
        }
    }
}
