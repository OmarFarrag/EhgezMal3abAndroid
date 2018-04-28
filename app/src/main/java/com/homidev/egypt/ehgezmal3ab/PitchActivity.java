package com.homidev.egypt.ehgezmal3ab;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.N)
public class PitchActivity extends AppCompatActivity {

    private Pitch pitch;
    private ProgressDialog progressBar;
    private ConnectionManager connectionManager;
    private ArrayList<TimeSlot> timeSlots;
    private View.OnClickListener slotBtnListener;
    private String reservationStartsOn=null ;
    private String reservationEndsOn=null ;
    private String venueNameTitle;
    protected int selectedYear=Calendar.getInstance().get(Calendar.YEAR);
    protected int selectedMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
    protected int selectedDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    private PitchActivity instance;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_design);

       // TextView pitchNameTextView = findViewById(R.id.pitchActivityPitchName);

        instance=this;

        Bundle extras = getIntent().getExtras();

        setDatePickerListener();

        connectionManager = ConnectionManager.getConnectionManager();

        if(extras != null) {
            pitch = (Pitch) extras.get("pitchName");
            venueNameTitle = (String) extras.get("venueName");
        }

     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            connectionManager.getPitchSchedule(pitch.getVenueID(),pitch.getPitchName(),"2017-09-08T00:00:00",this);
        }*/

        DisplayVenueAndPitchNames();

        setSelectedDate(selectedYear,selectedMonth,selectedDay);

/*
        if(extras != null) {
            pitch = (Pitch) extras.get("pitchName");
        }
        if(pitch != null) {
            pitchNameTextView.setText(pitch.getPitchName());
        }*/
    }

    protected void setDatePickerListener()
    {
        final FloatingActionButton datePickerBtn = (FloatingActionButton) findViewById(R.id.datePickerBtn);
        datePickerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setCallerActivity(instance);
                ((DialogFragment)datePickerFragment).show(getFragmentManager(),"Change Date");
            }
        });
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
        scheduleLayout.removeAllViews();

        for(int i = 0; i<48; i++)
        {
            final Button slot = new Button(this);

                slot.setTextAppearance(this, R.style.TimeTableStyle);
                slot.setText(((timeSlots.get(i).getStartsOn().split("T"))[1]));

                if(timeSlots.get(i).isEmpty()) {
                    slot.setBackgroundColor(getResources().getColor(R.color.mainGreenLight));
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
            slot.setOnClickListener(new View.OnClickListener() {
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
                        slot.setBackgroundColor(getResources().getColor(R.color.selectedcolor));
                        currentTimeSlot.setSelected(true);
                        return;
                    }

                    //Here means the user has selected a time slot on this session

                    //Check if select or deselect
                    if(!currentTimeSlot.isSelected()) {

                        if (reservationStartsOn.equals(currentTimeSlot.getEndsOn())) {
                            reservationStartsOn = currentTimeSlot.getStartsOn();
                            slot.setBackgroundColor(getResources().getColor(R.color.selectedcolor));
                            currentTimeSlot.setSelected(true);

                        } else if (reservationEndsOn.equals(currentTimeSlot.getStartsOn())) {
                            reservationEndsOn = currentTimeSlot.getEndsOn();
                            slot.setBackgroundColor(getResources().getColor(R.color.selectedcolor));
                            currentTimeSlot.setSelected(true);

                        } else {
                            //This slot is not valid to choose
                            showToasMessage(getResources().getString(R.string.invalidSlotSTring));

                        }
                    }
                    else
                    {
                        //Deselect logic
                        if (reservationEndsOn.equals(currentTimeSlot.getEndsOn()) )
                        {
                            currentTimeSlot.setSelected(false);
                            slot.setBackgroundColor(getResources().getColor(R.color.mainGreenLight));
                            if(reservationStartsOn.equals(currentTimeSlot.getStartsOn()))
                            {
                                reservationStartsOn = null;
                                reservationEndsOn = null;
                            }
                            else {
                                reservationEndsOn = currentTimeSlot.getStartsOn();
                            }
                        }
                        else if (reservationStartsOn.equals(currentTimeSlot.getStartsOn()))
                        {
                            currentTimeSlot.setSelected(false);
                            slot.setBackgroundColor(getResources().getColor(R.color.mainGreenLight));
                            if(reservationEndsOn.equals(currentTimeSlot.getEndsOn()))
                            {
                                reservationStartsOn = null;
                                reservationEndsOn = null;
                            }
                            else {
                                reservationStartsOn = currentTimeSlot.getEndsOn();
                            }

                        }
                        else
                        {
                            showToasMessage(getResources().getString(R.string.invalidSlotSTring));
                        }

                    }
                    return;


                }
            });


           // LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                scheduleLayout.addView(slot);


        }
        dismissLoading();


    }

    protected void showToasMessage(String message)
    {
        Toast messageToast = null;
        messageToast=  Toast.makeText(this,message,Toast.LENGTH_SHORT);

        messageToast.show();
    }

    /*
     * gets the venue and pitch name from the pitch member and displays them
     */
    protected void DisplayVenueAndPitchNames()
    {
        TextView venueName = (TextView) findViewById(R.id.venueName);
        TextView pitchName = (TextView) findViewById(R.id.pitchName);
        venueName.setText(venueNameTitle);
        pitchName.setText(pitch.getPitchName());
    }

    public void setSelectedYear(int year){selectedYear=year;}
    public void setSelectedMonth(int month){selectedMonth=month;}
    public void setSelectedDay(int day){selectedDay=day;}

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setSelectedDate(int year, int month, int day)
    {
        setSelectedDay(day);
        setSelectedMonth(month);
        setSelectedYear(year);
        TextView dateText = (TextView) findViewById(R.id.selectedDateTxt);
        dateText.setText(String.valueOf(selectedDay)+"-"+String.valueOf(selectedMonth)+"-"+String.valueOf(selectedYear));

        showLoading();

        String selectedDate=getSelectedDate()+"T00:00:00";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            connectionManager.getPitchSchedule(pitch.getVenueID(),pitch.getPitchName(),selectedDate,this);
        }
    }


    /*
     * Returns the full selected date
     */
    protected String getSelectedDate()
    {
        String startsOnDate = String.valueOf(selectedYear);
        if(selectedMonth<10)startsOnDate+="-0"+String.valueOf(selectedMonth);
        else startsOnDate+="-"+String.valueOf(selectedMonth);
        if(selectedDay<10)startsOnDate+="-0"+String.valueOf(selectedDay);
        else startsOnDate+="-"+String.valueOf(selectedDay);

        return startsOnDate;
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        PitchActivity  callerActivity;

        public void setCallerActivity(PitchActivity caller)
        {
            callerActivity=caller;
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            callerActivity.setSelectedDate(year,month+1,day);
        }
    }


}


