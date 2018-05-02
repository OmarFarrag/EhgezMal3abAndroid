package com.homidev.egypt.ehgezmal3ab;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;


public class ReservationsFragment extends android.support.v4.app.Fragment {

    private RecyclerView reservationsRecyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private Toolbar reservationsToolbar;
    private android.support.v4.app.Fragment instance;
    private TextView totalReservationsPriceTxt;

    ConnectionManager connectionManager = ConnectionManager.getConnectionManager();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        instance = this;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rservationsView = inflater.inflate(R.layout.reservations_layout,container,false);

        reservationsRecyclerView = rservationsView.findViewById(R.id.reservationsRecyclerView);
        initializeReservationsList(rservationsView);
        return rservationsView;

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void initializeReservationsList(final View reservationsView)
    {
        //initialize recycler view
        reservationsRecyclerView = reservationsView.findViewById(R.id.reservationsRecyclerView);

        totalReservationsPriceTxt = (TextView)  reservationsView.findViewById(R.id.totalReservationsPriceTxt);

        //set fixed size for recycler view
        reservationsRecyclerView.setHasFixedSize(true);

        //Layout manager is responsible for positioning item views (venues for now) within the allVenuesRecyclerView
        reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final ReservationsFragment fragment = this;
        IRecyclerViewClickListener listener = null;
        if(!getContext().getSharedPreferences("appUserPrefs", MODE_PRIVATE).getString("token", "").equals(""))
        {
            listener = getAppUserListener();
        }
        else
        {
            listener = getVenAdminListener();
        }



        //create an adapter, automatically fires a GET request to get all venues(for now)
        recyclerAdapter = new ReservationItemAdapter(getContext(), listener, this);

        //set ReservationAdapter to adapt reservationsRecyclerView for displaying the venues(for now)
        reservationsRecyclerView.setAdapter(recyclerAdapter);
    }

    public void setToolbar(Toolbar f_toolbar)
    {

    }

    protected IRecyclerViewClickListener getAppUserListener()
    {
        final ReservationsFragment fragment = this;
        IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view, int position) {
                final Reservation reservation = ReservationItemAdapter.getItem(position);

                if(reservation.getStatus().equals("Pending")){
                    PopupMenu menu = new PopupMenu(getContext(), (CardView) view.findViewById(R.id.reservationCardView));
                    menu.getMenuInflater().inflate(R.menu.reservations_menu, menu.getMenu());
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.reservations_menu_cancel:
                                    connectionManager.cancelReservation(reservation,fragment);
                                    return true;
                                case R.id.reservations_menu_share:
                                    connectionManager.getReservationShareLink(reservation);
                                    return true;
                            }
                            return false;
                        }
                    });
                    menu.show();
                }
            }
        };

        return listener;
    }

    protected IRecyclerViewClickListener getVenAdminListener()
    {
        final ReservationsFragment fragment = this;
        IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view, int position) {
                final Reservation reservation = ReservationItemAdapter.getItem(position);


                if(reservation.getStatus().equals("Pending")){
                    PopupMenu menu = new PopupMenu(getContext(), (CardView) view.findViewById(R.id.reservationCardView));
                    menu.getMenuInflater().inflate(R.menu.admin_reservations_menu, menu.getMenu());
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.admin_reservations_menu_decline:
                                    reservation.setStatus("");
                                    connectionManager.declineReservation((ReservationsFragment) instance,reservation);
                                    return true;
                                case R.id.admin_reservations_menu_accept:
                                    reservation.setStatus("");
                                    connectionManager.acceptReservation((ReservationsFragment) instance,reservation);
                                    return true;
                            }
                            return false;
                        }
                    });
                    menu.show();
                }
            }
        };

        return listener;
    }


    public void showToasts(Error error, boolean cancelled){
        if(cancelled){
            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getContext(),error.getText(),Toast.LENGTH_LONG).show();
    }

    public void showToasMessage(String message)
    {
        Toast messageToast = null;
        messageToast=  Toast.makeText(getContext(),message,Toast.LENGTH_SHORT);

        messageToast.show();
    }


    //Function to set the value displayed for total price of reservations
    //Called by the ReservationsAdapter on changing the list of reservations
    public void setTotalReservationsPrice(int acceptedReservationsTotalPrice)
    {
        totalReservationsPriceTxt.setText(String.valueOf(acceptedReservationsTotalPrice));
    }
}
