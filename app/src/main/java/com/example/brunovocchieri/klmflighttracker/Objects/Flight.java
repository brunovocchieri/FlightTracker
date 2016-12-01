package com.example.brunovocchieri.klmflighttracker.Objects;

import com.example.brunovocchieri.klmflighttracker.Constants;

import java.io.Serializable;

/**
 * Created by Bruno Vocchieri on 23/10/2016.
 */

public class Flight implements Serializable {
    String flightNumber;
    FlightLeg operatingFlightLeg;

    public Flight(){}

    public String getFlightNumber() {
        return flightNumber;
    }


    public FlightLeg getOperatingFlightLeg() {
        return operatingFlightLeg;
    }

}
