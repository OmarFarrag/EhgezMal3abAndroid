package com.homidev.egypt.ehgezmal3ab;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class ReservationsFragment extends android.support.v4.app.Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View reservationsView = inflater.inflate(R.layout.reservations_layout, container,false);


        return reservationsView;
    }
}
