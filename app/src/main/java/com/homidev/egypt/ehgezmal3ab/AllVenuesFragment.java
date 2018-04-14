package com.homidev.egypt.ehgezmal3ab;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by User on 14/04/2018.
 */

public class AllVenuesFragment extends android.support.v4.app.Fragment {

    private RecyclerView allVenuesRecyclerView;
    private RecyclerView.Adapter recyclerAdapter;

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
        View allVenuesView = inflater.inflate(R.layout.all_venues_layout,container,false);

        allVenuesRecyclerView= allVenuesView.findViewById(R.id.allVenuesRecyclerView);
        initializeVenuesList(allVenuesView);
        return allVenuesView;

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void initializeVenuesList(View allVenuesView)
    {
        //initialize recycler view
        allVenuesRecyclerView = allVenuesView.findViewById(R.id.allVenuesRecyclerView);

        //set fixed size for recycler view
        allVenuesRecyclerView.setHasFixedSize(true);

        //Layout manager is responsible for positioning item views (venues for now) within the allVenuesRecyclerView
        allVenuesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //create an adapter, automatically fires a GET request to get all venues(for now)
        recyclerAdapter = new VenueItemAdapter(getContext());

        //set VenueItemAdapter to adapt allVenuesRecyclerView for displaying the venues(for now)
        allVenuesRecyclerView.setAdapter(recyclerAdapter);
    }
}
