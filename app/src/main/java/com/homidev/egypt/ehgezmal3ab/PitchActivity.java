package com.homidev.egypt.ehgezmal3ab;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

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
    private String promoCode;
    private RatingBar ratingBar;
    private Button submitButton;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_design);

        // TextView pitchNameTextView = findViewById(R.id.pitchActivityPitchName);

        instance=this;

        Bundle extras = getIntent().getExtras();

        ratingBar = findViewById(R.id.userRatePitchRB);
        submitButton = findViewById(R.id.submitPitchReviewBtn);

        setSubmitReviewButtonListener();

        setDatePickerListener();


        setReserveBtnListener();



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

        hildeSubmitRateForAdmin(ratingBar,submitButton);

/*
        if(extras != null) {
            pitch = (Pitch) extras.get("pitchName");
        }
        if(pitch != null) {
            pitchNameTextView.setText(pitch.getPitchName());
        }*/
    }

    private void hildeSubmitRateForAdmin(RatingBar ratingBar, Button submitButton)
    {
        if(!getSharedPreferences("venAdminPrefs",MODE_PRIVATE).getString("token","").equals(""))
        {
            ratingBar.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);
            TextView reviewLabel = (TextView) findViewById(R.id.reviewTxt);
            reviewLabel.setVisibility(View.GONE);
        }
    }

    protected void setSubmitReviewButtonListener() {

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username =  getSharedPreferences("appUserPrefs",MODE_PRIVATE).getString("username","");
                String venueID = pitch.getVenueID();
                String pitchName = pitch.getPitchName();
                PlayerSubmitReview playerSubmitReview = new PlayerSubmitReview(username,
                        venueID, pitchName, ratingBar.getRating());
                connectionManager.setNewPitchRating(playerSubmitReview);
                Toast.makeText(instance, "Pitch reviewed successfully: " + ratingBar.getRating() ,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void setReserveBtnListener()
    {
        final FloatingActionButton reserveBtn = (FloatingActionButton) findViewById(R.id.reserveBtn);
        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reservationEndsOn==null)
                {
                    showToastMessage(getResources().getString(R.string.reserveErrorMessage));
                    return;
                }
                else
                {

                    String username =  getSharedPreferences("appUserPrefs",MODE_PRIVATE).getString("username","");
                    //The user is player
                    if(!username.equals(""))
                    {
                        promoCode=null;
                        showPromoCodeDialog(username);

                    }
                    else {
                        username = getSharedPreferences("venAdminPrefs",MODE_PRIVATE).getString("username","");
                        Reservation myReservation = new Reservation(username,reservationStartsOn,reservationEndsOn,pitch.getVenueID(),pitch.getPitchName());
                        connectionManager.reserveByAdmin(myReservation, instance);

                    }




                }
            }
        });


    }

    /*
     * Calls the reserve function from the connection manager
     */
    private void playerReserve(String promoCode, String username)
    {

        Reservation myReservation = new     Reservation(username,reservationStartsOn,reservationEndsOn,pitch.getVenueID(),pitch.getPitchName(),promoCode);
        connectionManager.reserve(myReservation, instance);
    }

    /*
     * This function is called when the connection manager finishes a reservation request
     */
    public void successfulReservation()
    {
        showToastMessage(getResources().getString(R.string.successfulReservation));
    }

    /*
    * This function is called when the connection manager finishes a reservation request
    */
    public void unsuccessfulReservation()
    {
        showToastMessage(getResources().getString(R.string.unsuccessfulReservation));
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
                        if(!getSharedPreferences("venAdminPrefs",MODE_PRIVATE).getString("token","").equals("")) {
                            showTimeSlotDetails(currentTimeSlot.getUsername(), currentTimeSlot.getPhoneNumber());
                        }
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
                            showToastMessage(getResources().getString(R.string.invalidSlotSTring));

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
                            showToastMessage(getResources().getString(R.string.invalidSlotSTring));
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

    protected void showToastMessage(String message)
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

        reservationStartsOn=null;
        reservationEndsOn=null;
    }

    /*
     * Displays a dialog box for the user to enter promo code
     */
    protected void showPromoCodeDialog(final String username)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Promo code");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                promoCode = input.getText().toString();
                playerReserve(promoCode,username);
            }
        });
        builder.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                promoCode = input.getText().toString();
                playerReserve(promoCode,username);
                dialog.cancel();
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();

            }});

        builder.show();
    }

    /*
    * Displays a dialog box showing the reserving person info of a specified time slot
    */
    protected void showTimeSlotDetails(final String username, final String phoneNumber)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Details");

        final LinearLayout layout = new LinearLayout(this);

        final TextView usernameTxt = new TextView(this);
        usernameTxt.setText(username);

        final TextView phoneNumberTxt = new TextView(this);
        phoneNumberTxt.setText(phoneNumber);

        layout.addView(usernameTxt);
        layout.addView(phoneNumberTxt);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });


        builder.show();
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