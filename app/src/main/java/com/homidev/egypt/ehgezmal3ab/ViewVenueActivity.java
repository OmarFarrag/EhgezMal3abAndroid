package com.homidev.egypt.ehgezmal3ab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ViewVenueActivity extends AppCompatActivity {

    private ConnectionManager connectionManager;
    private RecyclerView pitchesRecyclerView;
    private RecyclerView.Adapter pitchItemAdapter;
    private String  venueID;
    private String  venueName;


    public ViewVenueActivity() {
        this.connectionManager = ConnectionManager.getConnectionManager();
    }


    /*
     * Create the view to be displayed
     * Initialize the recycler view of the pitches
     * get the passed info
     * Implement the listener to launch pitch activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_pitches_layout);

        pitchesRecyclerView = findViewById(R.id.allPitchesRecyclerView);

        pitchesRecyclerView.setHasFixedSize(true);

        pitchesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bundle extras = getIntent().getExtras();
        venueID = "";
        if (extras != null) {
            venueID = (extras.getString("venueID"));
            venueName = extras.getString("venueName");
        }

        IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view, int position) {
                launchPitchActivity(position);
            }


        };

        pitchItemAdapter = new PitchItemAdapter(this, venueID, listener);

        final ImageView imageView = findViewById(R.id.mainVenueImage);

        //Display the venue image
        Picasso
                .with(this)
                .load("http://i.imgur.com/AS65Kmg.jpg")
                .placeholder(R.drawable.close_icon)
                .error(R.drawable.close_icon)
                .into(imageView);

        pitchesRecyclerView.setAdapter(pitchItemAdapter);

    }


    //Launch the pitch activity
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void launchPitchActivity(int position) {
        Intent intent = new Intent(this, PitchActivity.class);
        intent.putExtra("pitchName", PitchItemAdapter.getItem(position));
        intent.putExtra("venueName", venueName);
        startActivity(intent);
    }
}
