package com.homidev.egypt.ehgezmal3ab;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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


    /*
     * Requests the reservation list from the server through the connection manager
     */
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

        //If the user is a player
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



    /*
     Creates the listener in case of player user
     Allows him to cancel or share a reservation
     If pending allow cancel
     If accepted allow share
     */
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
                }else if(reservation.getStatus().equals("Accepted")){
                    PopupMenu menu = new PopupMenu(getContext(), (CardView) view.findViewById(R.id.reservationCardView));
                    menu.getMenuInflater().inflate(R.menu.accepted_res_menu, menu.getMenu());
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.reservations_menu_share_accepted:
                                    connectionManager.getReservationShareLink(reservation);
                                    return true;
                                case R.id.reservations_menu_share_friends:
                                    createShareWithFriendMenu();
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


    /*
     * Called when a user clicks on share with friend
     */
    public void createShareWithFriendMenu(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        connectionManager.getMyFriends(new Callback<ArrayList<Friend>>() {
            @Override
            public void onResponse(Call<ArrayList<Friend>> call, Response<ArrayList<Friend>> response) {
                if(response.code() == 200){
                    RecyclerView recyclerView = new RecyclerView(getContext());
                    final FriendItemAdapter friendItemAdapter = new FriendItemAdapter(getContext(), false);
                    LinearLayoutManager layoutManager =
                            new LinearLayoutManager(getContext());
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                            getContext(),
                            layoutManager.getOrientation()
                    );
                    recyclerView.addItemDecoration(dividerItemDecoration);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(friendItemAdapter);
                    friendItemAdapter.setFriendsList(response.body());

                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.setTitle("Choose friend");
                    final Dialog d = builder.setView(recyclerView).create();
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(d.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    d.show();
                    d.getWindow().setAttributes(lp);
                    IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            Friend friend = friendItemAdapter.getItem(position);

                            d.cancel();
                        }
                    };
                    friendItemAdapter.setListener(listener);
                    recyclerView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Friend>> call, Throwable t) {

            }
        });
    }
    /*
     Creates the listener in case of venue Admin
     Allows him to accept or decline a reservation request
     For pending only
     */
    protected IRecyclerViewClickListener getVenAdminListener()
    {
        final ReservationsFragment fragment = this;
        IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view, int position) {
                final Reservation reservation = ReservationItemAdapter.getItem(position);

                if(reservation.getStatus().equals("Pending")){
                    //Creats the sub menu
                    PopupMenu menu = new PopupMenu(getContext(), (CardView) view.findViewById(R.id.reservationCardView));
                    menu.getMenuInflater().inflate(R.menu.admin_reservations_menu, menu.getMenu());
                    //Set listener for buttons
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


    //Shows toast message
    public void showToasts(Error error, boolean cancelled){
        if(cancelled){
            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getContext(),error.getText(),Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void notifyDataChange()
    {
        ((ReservationItemAdapter)recyclerAdapter).refreshData();
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
