package com.homidev.egypt.ehgezmal3ab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.zip.Inflater;

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

        pitchItemAdapter = new PitchItemAdapter(this);

        pitchesRecyclerView.setAdapter(pitchItemAdapter);


    }
}
