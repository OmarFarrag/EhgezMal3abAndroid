package com.homidev.egypt.ehgezmal3ab;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by engineer on 27/03/18.
 * this class adapts Venue to use it in recyclerview
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ReservationItemAdapter extends RecyclerView.Adapter<ReservationItemAdapter.ReservationItemViewHolder> {

    private ConnectionManager connectionManager;
    private static List<Reservation> reservationList;
    private Context context;
    private IRecyclerViewClickListener listener;

    public ReservationItemAdapter(Context context, IRecyclerViewClickListener listener) {
        this.context = context;
        connectionManager = ConnectionManager.getConnectionManager();
        //reservationList= connectionManager.getPlayerReservations();
        connectionManager.getReservations(this);
        reservationList = new ArrayList<>();
        this.listener = listener;
    }

    //Called when RecyclerView needs a new VenueItemHolder to represent a venue item.
    @Override
    public ReservationItemAdapter.ReservationItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reservation_design_layout, parent, false);
        return new ReservationItemViewHolder(view, listener);
    }

    //Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(ReservationItemAdapter.ReservationItemViewHolder holder, int position) {
        if(holder instanceof ReservationItemViewHolder) {
            Reservation reservation = reservationList.get(position);
            //set the attributes of the venue item to be displayed
            holder.reservationDate.setText(reservation.getStartsOn().toString());
            holder.reservationVenue.setText(reservation.getVenueID());
            holder.reservationPitch.setText(reservation.getPitchName());
        }
    }

    public static ArrayList<Reservation> getVenuesList() {
        return (ArrayList<Reservation>) reservationList;
    }

    public void setReservationList(ArrayList<Reservation> reservations){
        reservationList = reservations;
        this.notifyDataSetChanged();
    }

    public static Reservation getItem(int position) {
        return reservationList.get(position);
    }

    @Override
    public int getItemCount() {
        if(reservationList == null){
           return 0;
        }
        return reservationList.size();
    }


    //A ViewHolder describes an item view and metadata about its place within the RecyclerView
    public class ReservationItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView reservationDate;
        TextView reservationVenue;
        TextView reservationPitch;
        private IRecyclerViewClickListener listener;
        CardView cardView;

        public ReservationItemViewHolder(View itemView, IRecyclerViewClickListener listener) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.reservationCardView);
            reservationDate = (TextView) itemView.findViewById(R.id.reservationDate);
            reservationVenue = (TextView) itemView.findViewById(R.id.reservationVenue);
            reservationPitch = (TextView) itemView.findViewById(R.id.reservationPitch);
            this.listener = listener;
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }
}
