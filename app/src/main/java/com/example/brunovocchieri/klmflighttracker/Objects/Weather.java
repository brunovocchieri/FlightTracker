package com.example.brunovocchieri.klmflighttracker.Objects;

import java.io.Serializable;

/**
 * Created by Bruno Vocchieri on 23/10/2016.
 */
public class Weather implements Serializable {

    String degrees;
    String extra_info;

    public Weather(String degrees, String extra_info) {
        this.degrees = degrees;
        this.extra_info = extra_info;
    }

    public String getDegrees() {
        return degrees;
    }

    public String getExtra_info() {
        return extra_info;
    }
}
