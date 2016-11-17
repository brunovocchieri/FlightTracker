package com.example.brunovocchieri.klmflighttracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.example.brunovocchieri.klmflighttracker.Objects.FlightsList;
import com.example.brunovocchieri.klmflighttracker.Tasks.ServerTask;
import com.example.brunovocchieri.klmflighttracker.Tasks.TaskManager;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements TaskManager.TaskListener,
        CalendarDatePickerDialogFragment.OnDateSetListener {

    @Bind(R.id.etFlight)       EditText etFlight;
    @Bind(R.id.etSelectDate)   EditText etSelectDate;
    @Bind(R.id.etDeparture)    AutoCompleteTextView etDeparture;
    @Bind(R.id.etDestination)  AutoCompleteTextView etDestination;
    @Bind(R.id.tvSearch)       TextView tvSearch;
    @BindString(R.string.invalid_value) String ERROR_INVALID_VALUE;
    @BindString(R.string.insuficient_info) String ERROR_INSUFICIENT_INFORMATION;


    @OnClick(R.id.etSelectDate)
    public void onClickSelectDate(){
        startDatePicker();
    }

    @OnClick(R.id.tvSearch)
    public void onClickSearch(){

        String flight_number = etFlight.getText().toString();
        String selected_date = etSelectDate.getText().toString();
        String departure = etDeparture.getText().toString();
        String destination = etDestination.getText().toString();

        //Perform search using typed info.
        performSearch(flight_number, selected_date, departure, destination);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);//inicializa tudo o q esta encima

        etFlight.setNextFocusDownId(R.id.etSelectDate);

        setOnFocusChangeListener(etSelectDate);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, Constants.airports_locations);
        etDeparture.setAdapter(adapter);
        etDeparture.setThreshold(1);
        etDeparture.setOnItemClickListener(OnItemClickListener(etDeparture));

        etDestination.setAdapter(adapter);
        etDestination.setThreshold(1);
        etDestination.setOnItemClickListener(OnItemClickListener(etDestination));

    }

    /**
     * This method is responsible for setting OnFocusListener to the EditText Select Date.
     * Setting this listener is important to improve the user experience in the app.
     * What it does is, when the user finish typing the Flight Number and tap "Enter" on
     * the keyboard, the Select Date gets the focus and automatically starts the DatePicker.
     */
    public void setOnFocusChangeListener(EditText et){

        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    startDatePicker();
                }
            }
        });
    }


    /**
     * This method is responsible for setting OnItemClickListener to the EditText Departure
     * and Destination.
     * Setting this listener is important to improve the user experience in the app.
     * What it does is, when the user tap a departure or destination sugestion, it
     * move the focus to the next view to receive the action.
     */
    public AdapterView.OnItemClickListener OnItemClickListener(AutoCompleteTextView tv){

        AdapterView.OnItemClickListener listener = null;

        switch (tv.getId()){
            case R.id.etDeparture:
                listener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        etDestination.requestFocus();
                    }
                };
                break;

            case R.id.etDestination:
                listener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(etDestination.getWindowToken(), 0);
                        tvSearch.requestFocus();
                    }
                };
                break;
        }

        return listener;
    }


    /**
     * This method is called to initialize the DatePicker when Select Date receive the focus
     * or is clicked by the user.
     */
    public void startDatePicker(){
        CalendarDatePickerDialogFragment datePicker = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(this)
                .setFirstDayOfWeek(Calendar.SUNDAY);
        datePicker.show(getSupportFragmentManager(), null);
    }


    /**
     * Overrided method from DatePicker.
     * This method is called every time the user finishes a date selection in the calendar.
     */
    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        String day = (dayOfMonth < 10) ? "0" + dayOfMonth : "" + dayOfMonth;
        String month = (monthOfYear < 10) ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1);

        etSelectDate.setText(year + "-" + month + "-" + day);
    }


    /**
     * Method called to perfom search for flight using data provided by the user.
     */
    public void performSearch(String flight_number, String selected_date, String departure, String destination){

        //Verifies is flight_number and selected_date aren't empty
        if (!flight_number.trim().isEmpty() && !selected_date.trim().isEmpty()) {

            new ServerTask(this).getFlightsList("departureDate=" + selected_date + "&flightNumber=" + flight_number, false, "SINGLE_FLIGHT");

        }
        //Verifies if departure and destination aren' empty.
        else if (!departure.trim().isEmpty() && !destination.trim().isEmpty()) {

            //Verifies if departure and destination are a valid value.
            if (departure.contains(",") && destination.contains(",")) {

                //Select only the departure and destination IATACode.
                departure = departure.substring(0, departure.indexOf(","));
                destination = destination.substring(0, destination.indexOf(","));

                //Perform search by departure and destination
                new ServerTask(this).getFlightsList("originAirportCode=" + departure + "&destinationAirportCode=" + destination, false, "LIST_FLIGHT");

            }
            else{
                //Tells the user that origin or destination is invalid.
                Toast.makeText(MainActivity.this, ERROR_INVALID_VALUE, Toast.LENGTH_SHORT).show();
            }
        }
        else{
            //Tells the user that some required info is missing.
            Toast.makeText(MainActivity.this, ERROR_INSUFICIENT_INFORMATION, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void performTask(Object responseObject, String tag) {
        FlightsList flightsList = (FlightsList) responseObject;

        if (tag.equals("SINGLE_FLIGHT")){
            startActivity(new Intent(this, DetailsActivity.class).putExtra("details", flightsList.getFlights().get(0)));
        }
        else {
            startActivity(new Intent(this, FlightsListActivity.class).putExtra("flights", flightsList.getFlights()));
        }
    }

    @Override
    public void onRequestError() {
        Toast.makeText(this, "Flight not found.", Toast.LENGTH_SHORT).show();
    }
}
