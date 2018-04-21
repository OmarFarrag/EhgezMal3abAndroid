package com.homidev.egypt.ehgezmal3ab;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.zip.Inflater;

public class ViewVenueFragment extends  android.support.v4.app.Fragment{

    private ConnectionManager connectionManager;
    private RecyclerView pitchesRecyclerView;
    private RecyclerView.Adapter pitchItemAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View venuPitchesView = inflater.inflate(R.layout.all_pitches_layout,container,false);

        pitchesRecyclerView = venuPitchesView.findViewById(R.id.allPitchesRecyclerView);

        pitchesRecyclerView.setHasFixedSize(true);

        pitchesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Bundle extras = getActivity().getIntent().getExtras();
        String venueID = "";
        if (extras != null) {
            venueID = (extras.getString("venueID"));
        }

        IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view, int position) {
                launchPitchActivity(position);
            }
        };

        pitchItemAdapter = new PitchItemAdapter(getContext(), venueID, listener);

        final ImageView imageView = venuPitchesView.findViewById(R.id.mainVenueImage);

        Picasso
                .with(getContext())
                .load("http://i.imgur.com/AS65Kmg.jpg")
                .placeholder(R.drawable.close_icon)
                .error(R.drawable.close_icon)
                .into(imageView);

        pitchesRecyclerView.setAdapter(pitchItemAdapter);

        return venuPitchesView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void launchPitchActivity(int position) {
        Intent intent = new Intent(getContext(), PitchActivity.class);
        intent.putExtra("pitchName", PitchItemAdapter.getItem(position));
        startActivity(intent);
    }
}
