package com.homidev.egypt.ehgezmal3ab;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 * This class implements the fragment that shows all the venues in the system for the user
 */

public class AllVenuesFragment extends android.support.v4.app.Fragment {

    private RecyclerView allVenuesRecyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private Toolbar allVenuesToolbar;

    ConnectionManager connectionManager = ConnectionManager.getConnectionManager();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    /*
     * This function is called to display the UI components to the user
     * First we will inflate the view, then initialize the list of the recyclerView list
     * The caller is expecting the function to return the view to be displayed so at the end we return the all venues view
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        //Inflating the view
        View allVenuesView = inflater.inflate(R.layout.all_venues_layout,container,false);

        //Extracting the recyclerView and calling its initialization function
        allVenuesRecyclerView = allVenuesView.findViewById(R.id.allVenuesRecyclerView);
        initializeVenuesList(allVenuesView);

        return allVenuesView;

    }



    /*
     * This function is called to initialize the list of the venues implemented by the RecyclerView
     * The recycler view is linked with its supporting utilities (LayoutManager and Adapter)
     * The click listener for the list items will be created here and passed to the adapter constructor
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void initializeVenuesList(final View allVenuesView)
    {
        //initialize recycler view
        allVenuesRecyclerView = allVenuesView.findViewById(R.id.allVenuesRecyclerView);

        //set fixed size for recycler view
        allVenuesRecyclerView.setHasFixedSize(true);

        //Layout manager is responsible for positioning item views (venues for now) within the allVenuesRecyclerView
        allVenuesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Implementing the listener to be passed to the adapter
        IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                //On click, launch a new Pitch activity for the selected pitch
                Intent intent = new Intent(getActivity(), ViewVenueActivity.class);
                String venueID = VenueItemAdapter.getItem(position).getVenueID();
                String venueName = VenueItemAdapter.getItem(position).getVenueTitle();
                intent.putExtra("venueID", venueID);
                intent.putExtra("venueName",venueName);
                startActivity(intent);
            }
        };

        //create an adapter, automatically fires a GET request to get all venues(for now)
        recyclerAdapter = new VenueItemAdapter(getContext(), listener);

        //set VenueItemAdapter to adapt allVenuesRecyclerView for displaying the venues(for now)
        allVenuesRecyclerView.setAdapter(recyclerAdapter);
    }

    public void setToolbar(Toolbar f_toolbar)
    {
        allVenuesToolbar = f_toolbar;
    }



}
