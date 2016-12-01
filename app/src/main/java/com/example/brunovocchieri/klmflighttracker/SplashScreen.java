package com.example.brunovocchieri.klmflighttracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.brunovocchieri.klmflighttracker.Objects.Airport;
import com.example.brunovocchieri.klmflighttracker.Tasks.ServerTask;
import com.example.brunovocchieri.klmflighttracker.Tasks.TaskManager;

import java.util.ArrayList;

/**
 * Created by Bruno Vocchieri on 23/10/2016.
 */
public class SplashScreen extends AppCompatActivity implements TaskManager.TaskListener{

    private static final String AIRPORTS_REQUEST_URL = "https://raw.githubusercontent.com/jpatokal/openflights/master/data/airports.dat";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);   		 requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        new ServerTask(this).getAirportList(AIRPORTS_REQUEST_URL, true, "AIRPORT_LIST");

    }


    @Override
    public void performTask(Object responseObject, String tag) {
        String airportResponse = (String) responseObject;

        String[] lines = airportResponse.replace("\"", "").split("\\r?\\n");
        String[] data;
        String code;
        ArrayList<Airport> airports = new ArrayList<>();

        for(String line : lines){
            data = line.split(",");

            //Get IATACode if not empty. Otherwise, get 4 digits code.
            code = (!data[4].isEmpty()) ? data[4] : data[5];

            //index 2 = City; index 3 = country; index 4 = IATACode; index 6 = latitude; index 7 = longitude
            airports.add(new Airport(data[2], data[3], code, data[6], data[7]));
        }

        Constants.airports = airports;

        //Create a String Array with origins and destinations suggestions.
        Constants.airports_locations = new String[airports.size()];
        for (int i = 0; i < airports.size(); i++){
            Constants.airports_locations[i] = airports.get(i).getCode() + ", " + airports.get(i).getCity() + ", " + airports.get(i).getCountry();
        }

        //Start MainActivity
        startActivity(new Intent(SplashScreen.this, MainActivity.class));
        finish();
    }

    @Override
    public void onRequestError() {

    }
}
