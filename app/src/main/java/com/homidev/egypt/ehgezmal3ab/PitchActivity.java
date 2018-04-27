package com.homidev.egypt.ehgezmal3ab;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class PitchActivity extends AppCompatActivity {

    private Pitch pitch;
    private ProgressDialog progressBar;
    private ConnectionManager connectionManager;
    private ArrayList<TimeSlot> timeSlots;
    private View.OnClickListener slotBtnListener;
    private String reservationStartsOn=null ;
    private String reservationEndsOn=null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_design);

       // TextView pitchNameTextView = findViewById(R.id.pitchActivityPitchName);

        Bundle extras = getIntent().getExtras();

        showLoading();

        connectionManager = ConnectionManager.getConnectionManager();

        if(extras != null) {
            pitch = (Pitch) extras.get("pitchName");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            connectionManager.getPitchSchedule(pitch.getVenueID(),pitch.getPitchName(),"2017-09-08T00:00:00",this);
        }

/*
        if(extras != null) {
            pitch = (Pitch) extras.get("pitchName");
        }
        if(pitch != null) {
            pitchNameTextView.setText(pitch.getPitchName());
        }*/
    }

    protected void showLoading()
    {
        progressBar = new ProgressDialog(this);
        progressBar.setTitle("Loading");
        progressBar.setMessage("Wait while loading...");
        progressBar.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progressBar.show();
    }

    protected void dismissLoading()
    {
        progressBar.dismiss();
    }

    /*
     * This functions gets called by the connection manager after getting the schedule of the pitch
     */
    public void ShowSchedule(ArrayList<TimeSlot> f_timeSlots)
    {
        this.timeSlots = f_timeSlots;

        LinearLayout scheduleLayout = (LinearLayout) findViewById(R.id.scheduleLayout);

        for(int i = 0; i<48; i++)
        {
            final Button slot = new Button(this);

                slot.setTextAppearance(this, R.style.TimeTableStyle);
                slot.setText(((timeSlots.get(i).getStartsOn().split("T"))[1]));

                if(timeSlots.get(i).isEmpty()) {
                    slot.setBackgroundColor(getResources().getColor(R.color.mainGreen));
                }
                else
                {
                    slot.setBackgroundColor(getResources().getColor(R.color.declinedColor));
                }

            final TimeSlot currentTimeSlot = timeSlots.get(i);
            final TimeSlot nextTimeSlot;
            if(i<47)    {   nextTimeSlot= timeSlots.get(i+1); }
            else
            {
                nextTimeSlot=null;
            }
            final TimeSlot prevTimeSlot;
            if(i>0){ prevTimeSlot= timeSlots.get(i-1);}
            else { prevTimeSlot = null;}
          /*  slot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!currentTimeSlot.isEmpty())
                    {
                        return;
                    }

                    //Check if there are no selected slots, then initialize the reservations start and end
                    if(reservationStartsOn==null)
                    {
                        reservationStartsOn=currentTimeSlot.getStartsOn();
                        reservationEndsOn = currentTimeSlot.getEndsOn();
                        slot.setBackgroundColor(getResources().getColor(R.color.selectedColor));
                    }


                }
            });
            */

           // LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                scheduleLayout.addView(slot);


        }
        dismissLoading();


    }


}
