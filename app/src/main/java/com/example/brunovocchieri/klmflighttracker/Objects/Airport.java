package com.example.brunovocchieri.klmflighttracker.Objects;

/**
 * Created by Bruno Vocchieri on 23/10/2016.
 */
public class Airport {

    String city;
    String country;
    String code;
    String lat;
    String lon;

    public Airport(String city, String country, String code, String lat, String lon) {
        this.city = city;
        this.country = country;
        this.code = code;
        this.lat = lat;
        this.lon = lon;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {return country;}

    public String getCode() {
        return code;
    }

}
