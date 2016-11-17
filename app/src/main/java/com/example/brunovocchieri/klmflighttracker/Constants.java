package com.example.brunovocchieri.klmflighttracker;


import com.example.brunovocchieri.klmflighttracker.Objects.Airport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Bruno Vocchieri on 23/10/2016.
 */
public class Constants {

    public static ArrayList<Airport> airports = new ArrayList<>();
    public static String[] airports_locations;


    public static String formatDate(String dateToFormat){

        int indexT = dateToFormat.indexOf("T");
        String date = dateToFormat.substring(0, indexT );
        String time = dateToFormat.substring(indexT + 1, indexT + 6);

        final String NEW_FORMAT = "dd/MM/yyyy";
        final String OLD_FORMAT = "yyyy-MM-dd";

        String newDateString;

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern(NEW_FORMAT);
        newDateString = sdf.format(d);

        return newDateString + "\n" + time;

    }

}
