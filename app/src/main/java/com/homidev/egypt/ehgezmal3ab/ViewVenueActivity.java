package com.homidev.egypt.ehgezmal3ab;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ViewVenueActivity extends AppCompatActivity {

    private ConnectionManager connectionManager;
    private RecyclerView pitchesRecyclerView;
    private RecyclerView.Adapter pitchItemAdapter;

    public ViewVenueActivity() {
        this.connectionManager = ConnectionManager.getConnectionManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_venue);

        pitchesRecyclerView = findViewById(R.id.allPitchesRecyclerView);

        pitchesRecyclerView.setHasFixedSize(true);

        pitchesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bundle extras = getIntent().getExtras();
        int venueID = 0;
        if (extras != null) {
            venueID = Integer.parseInt(extras.getString("venueID"));
        }

        IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view, int position) {
                launchPitchActivity(position);
            }
        };

        pitchItemAdapter = new PitchItemAdapter(this, venueID, listener);

        pitchesRecyclerView.setAdapter(pitchItemAdapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void launchPitchActivity(int position) {
        Intent intent = new Intent(this, PitchActivity.class);
        String pitchName = PitchItemAdapter.getItem(position).getPitchTitle();
        intent.putExtra("pitchName", pitchName);
        startActivity(intent);
    }
}
