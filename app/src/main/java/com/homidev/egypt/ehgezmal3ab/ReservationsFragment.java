package com.homidev.egypt.ehgezmal3ab;

import android.content.ClipData;

import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class ReservationsFragment extends android.support.v4.app.Fragment {

    private RecyclerView reservationsRecyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private Toolbar reservationsToolbar;

    ConnectionManager connectionManager = ConnectionManager.getConnectionManager();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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

        //set fixed size for recycler view
        reservationsRecyclerView.setHasFixedSize(true);

        //Layout manager is responsible for positioning item views (venues for now) within the allVenuesRecyclerView
        reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final ReservationsFragment fragment = this;
        IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
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



        //create an adapter, automatically fires a GET request to get all venues(for now)
        recyclerAdapter = new ReservationItemAdapter(getContext(), listener, this);

        //set ReservationAdapter to adapt reservationsRecyclerView for displaying the venues(for now)
        reservationsRecyclerView.setAdapter(recyclerAdapter);
    }

    public void setToolbar(Toolbar f_toolbar)
    {

    }


    public void showToasts(Error error, boolean cancelled){
        if(cancelled){
            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getContext(),error.getText(),Toast.LENGTH_LONG).show();
    }
}
