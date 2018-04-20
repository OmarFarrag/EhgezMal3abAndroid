package com.homidev.egypt.ehgezmal3ab;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class PitchActivity extends AppCompatActivity {

    private Pitch pitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch);

       // TextView pitchNameTextView = findViewById(R.id.pitchActivityPitchName);

        Bundle extras = getIntent().getExtras();
/*
        if(extras != null) {
            pitch = (Pitch) extras.get("pitchName");
        }
        if(pitch != null) {
            pitchNameTextView.setText(pitch.getPitchName());
        }*/
    }
}
