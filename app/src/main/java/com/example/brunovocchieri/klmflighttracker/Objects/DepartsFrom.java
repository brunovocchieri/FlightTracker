package com.example.brunovocchieri.klmflighttracker.Objects;

import java.io.Serializable;

/**
 * Created by Bruno Vocchieri on 23/10/2016.
 */

    public class DepartsFrom implements Serializable {


        String IATACode;

        public String getIATACode() {
            return IATACode;
        }

        public void setIATACode(String IATACode) {
            this.IATACode = IATACode;
        }
    }

