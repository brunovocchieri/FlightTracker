package com.example.brunovocchieri.klmflighttracker.Tasks;

import com.example.brunovocchieri.klmflighttracker.Objects.FlightsList;

/**
 * Created by Bruno Vocchieri on 23/10/2016.
 */
public class ServerTask extends TaskManager {

    public ServerTask(TaskListener taskListener) {
        super(taskListener);
    }


    public void getFlightsList(String url, boolean asString, String tag) {
        doRequest(url, new FlightsList(), asString, tag);
    }

    public void getAirportList(String url, boolean asString, String tag) {
        doRequest(url, new FlightsList(), asString, tag);
    }

}