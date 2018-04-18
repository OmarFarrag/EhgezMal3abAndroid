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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_pitches_layout);

        TextView pitchNameTextView = findViewById(R.id.activityPitchNameTextView);

        Bundle extras = getIntent().getExtras();
        String pitchName = "";
        if(extras != null) {
            pitchName = extras.getString("pitchName");
        }

        pitchNameTextView.setText(pitchName);
    }
}
