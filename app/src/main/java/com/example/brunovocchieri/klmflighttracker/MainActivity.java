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
    @BindString(R.string.invalid_info)String ERROR_INVALID_AIRPORT;


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
        ButterKnife.bind(this);

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

    public void startDatePicker(){
        CalendarDatePickerDialogFragment datePicker = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(this)
                .setFirstDayOfWeek(Calendar.SUNDAY);
        datePicker.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        String day = (dayOfMonth < 10) ? "0" + dayOfMonth : "" + dayOfMonth;
        String month = (monthOfYear < 10) ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1);

        etSelectDate.setText(year + "-" + month + "-" + day);
    }


    /**
     * Method called to perform search for flight using data provided by the user.
     */
    public void performSearch(String flight_number, String selected_date, String departure, String destination){

        //Verifies is flight_number and selected_date aren't empty
        if (!flight_number.trim().isEmpty() && !selected_date.trim().isEmpty()) {

            new ServerTask(this).getFlightsList("departureDate=" + selected_date + "&flightNumber=" + flight_number, false, "SINGLE_FLIGHT");

        }
        //Verifies if departure and destination aren't empty.
        else if (!departure.trim().isEmpty() && !destination.trim().isEmpty()) {

            //Verifies if departure and destination are a valid value.
            if (departure.contains(",") && destination.contains(",")) {

                //Select only the departure and destination IATACode.
                departure = departure.substring(0, departure.indexOf(","));
                destination = destination.substring(0, destination.indexOf(","));

                if( departure.contains("AMS") || destination.contains("AMS")) {
                    //Perform search by departure and destination
                    new ServerTask(this).getFlightsList("originAirportCode=" + departure + "&destinationAirportCode=" + destination, false, "LIST_FLIGHT");
                }
                    else{
                        Toast.makeText(MainActivity.this, ERROR_INVALID_AIRPORT, Toast.LENGTH_SHORT).show();
                    }
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
        Toast.makeText(this, "Flight not found or not exist.", Toast.LENGTH_SHORT).show();
    }
}
