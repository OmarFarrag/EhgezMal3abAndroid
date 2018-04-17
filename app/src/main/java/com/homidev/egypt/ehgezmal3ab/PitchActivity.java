package com.homidev.egypt.ehgezmal3ab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PitchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch);

        TextView pitchNameTextView = findViewById(R.id.pitchActivityPitchName);

        Bundle extras = getIntent().getExtras();
        String pitchName = "";
        if(extras != null) {
            pitchName = extras.getString("pitchName");
        }

        pitchNameTextView.setText(pitchName);



    }
}
