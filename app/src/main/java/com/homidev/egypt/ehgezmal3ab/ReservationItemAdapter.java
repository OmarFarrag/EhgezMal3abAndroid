package com.homidev.egypt.ehgezmal3ab;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
    private View.OnClickListener cancelListener;
    private int acceptedReservationsTotalPrice;

    private ReservationsFragment fragment;

    public ReservationItemAdapter(Context context, IRecyclerViewClickListener listener, ReservationsFragment fragment) {
        this.context = context;
        connectionManager = ConnectionManager.getConnectionManager();
        //reservationList= connectionManager.getPlayerReservations();
        connectionManager.getReservations(this);
        reservationList = new ArrayList<>();
        this.listener = listener;
        this.fragment = fragment;
        setCancelListener();
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
            final Reservation reservation = reservationList.get(position);
            //set the attributes of the venue item to be displayed
            String[] timeParts = reservation.getStartsOn().toString().split("T");
            holder.reservationDate.setText(timeParts[1].split(":")[0]+":"+timeParts[1].split(":")[1]+" "+timeParts[0]);
            holder.reservationPrice.setText(String.valueOf(reservation.getPrice())+" L.E");
            holder.reservationVenue.setText(reservation.getVenueID());
            holder.reservationPitch.setText(reservation.getPitchName());

            //Change the background color according to the status and allow cancel action for pending
            if(reservation.getStatus().toLowerCase().equals("cancelled")|| reservation.getStatus().toLowerCase().equals("rejected"))
            {
                holder.layout.setBackgroundColor(context.getResources().getColor(R.color.declinedColor));
                holder.cancelBtn.setVisibility(View.GONE);
                holder.cancelBtn.setClickable(false);
            }
            else if (reservation.getStatus().toLowerCase().equals("pending"))
            {
                holder.layout.setBackgroundColor(context.getResources().getColor(R.color.pendingColor));
                holder.cancelBtn.setVisibility(View.VISIBLE);
                holder.cancelBtn.setClickable(true);
                holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        connectionManager.cancelReservation(reservation, fragment);
                    }
                });

            }
            else
            {
                holder.layout.setBackgroundColor(context.getResources().getColor(R.color.mainGreen));
                holder.cancelBtn.setVisibility(View.GONE);
                holder.cancelBtn.setClickable(false);
            }
        }
    }

    /*
    * The listener for the action of clicking the cancel btn
    * The reservation shall be canceled
     */
    private void setCancelListener ()
    {
        cancelListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    public static ArrayList<Reservation> getVenuesList() {
        return (ArrayList<Reservation>) reservationList;
    }

    public void setReservationList(ArrayList<Reservation> reservations){
        reservationList = reservations;
        this.notifyDataSetChanged();

        displayTotalReservationsPrice();
    }

    public void refreshData()
    {
        connectionManager.getReservations(this);
    }

    /*
     * This function is called when the list of reservations change, so the total price changes , so UI is updated
     */
    protected void displayTotalReservationsPrice()
    {
        acceptedReservationsTotalPrice = 0;
        for(int i =0; i<reservationList.size(); i++)
        {
            if(reservationList.get(i).getStatus().toLowerCase().equals("accepted"))
            {
                acceptedReservationsTotalPrice += reservationList.get(i).getPrice();
            }
        }

        fragment.setTotalReservationsPrice(acceptedReservationsTotalPrice);
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

    protected void cancelReservation()
    {

    }


    //A ViewHolder describes an item view and metadata about its place within the RecyclerView
    public class ReservationItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView reservationDate;
        TextView reservationPrice;
        TextView reservationVenue;
        TextView reservationPitch;
        ImageButton cancelBtn;
        RelativeLayout layout;
        private IRecyclerViewClickListener listener;
        CardView cardView;

        public ReservationItemViewHolder(View itemView, IRecyclerViewClickListener listener) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.reservationCardView);
            reservationDate = (TextView) itemView.findViewById(R.id.reservationDate);
            reservationPrice = (TextView) itemView.findViewById(R.id.reservationPrice);
            reservationVenue = (TextView) itemView.findViewById(R.id.reservationVenue);
            reservationPitch = (TextView) itemView.findViewById(R.id.reservationPitch);
            cancelBtn = (ImageButton)  itemView.findViewById(R.id.cancelBtn);
            this.listener = listener;
            cardView.setOnClickListener(this);
            layout = (RelativeLayout) itemView.findViewById(R.id.reservationLayout);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }
}
