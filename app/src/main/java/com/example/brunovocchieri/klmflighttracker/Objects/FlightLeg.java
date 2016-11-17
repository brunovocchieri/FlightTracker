package com.example.brunovocchieri.klmflighttracker.Objects;

import java.io.Serializable;

/**
 * Created by Bruno Vocchieri on 23/10/2016.
 */
public class FlightLeg implements Serializable {


    DepartsFrom departsFrom;
    ArrivesOn arrivesOn;
    String scheduledDepartureDateTime;
    String scheduledArrivalDateTime;
    String flightStatus;


    public DepartsFrom getDepartsFrom() {
        return departsFrom;
    }

    public void setDepartsFrom(DepartsFrom departsFrom) {
        this.departsFrom = departsFrom;
    }

    public ArrivesOn getArrivesOn() {
        return arrivesOn;
    }

    public String getScheduledDepartureDateTime() {
        return scheduledDepartureDateTime;
    }

    public void setScheduledDepartureDateTime(String scheduledDepartureDateTime) {
        this.scheduledDepartureDateTime = scheduledDepartureDateTime;
    }

    public String getScheduledArrivalDateTime() {
        return scheduledArrivalDateTime;
    }

    public void setScheduledArrivalDateTime(String scheduledArrivalDateTime) {
        this.scheduledArrivalDateTime = scheduledArrivalDateTime;
    }

    public String getFlightStatus() {
        return flightStatus;
    }

    public void setFlightStatus(String flightStatus) {
        this.flightStatus = flightStatus;
    }

    public void setArrivesOn(ArrivesOn arrivesOn) {
        this.arrivesOn = arrivesOn;
    }
}

