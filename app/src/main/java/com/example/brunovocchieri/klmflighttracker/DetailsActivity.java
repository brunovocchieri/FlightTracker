package com.example.brunovocchieri.klmflighttracker;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brunovocchieri.klmflighttracker.Objects.Flight;
import com.example.brunovocchieri.klmflighttracker.Tasks.GetDestinationImage;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bruno Vocchieri on 23/10/2016.
 */
public class DetailsActivity extends AppCompatActivity {
    @Bind(R.id.ivPhoto)
    ImageView ivPhoto;
    @Bind(R.id.tvDeparture)    TextView tvDeparture;
    @Bind(R.id.tvArrival)      TextView tvArrival;
    @Bind(R.id.tvDepartDate)   TextView tvDepartDate;
    @Bind(R.id.tvArrivalDate)  TextView tvArrivalDate;
    @Bind(R.id.tvStatus)       TextView tvStatus;
    @Bind(R.id.tvBack)         TextView tvBack;

    Flight details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        details = (Flight) getIntent().getSerializableExtra("details");

        initUI();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void initUI(){

        tvDeparture.setText(details.getOperatingFlightLeg().getDepartsFrom().getIATACode());
        tvArrival.setText(details.getOperatingFlightLeg().getArrivesOn().getIATACode());
        tvDepartDate.setText(Constants.formatDate(details.getOperatingFlightLeg().getScheduledDepartureDateTime()));
        tvArrivalDate.setText(Constants.formatDate(details.getOperatingFlightLeg().getScheduledArrivalDateTime()));
        tvStatus.setText(details.getOperatingFlightLeg().getFlightStatus().replace("_", " "));

        String destination_code = details.getOperatingFlightLeg().getArrivesOn().getIATACode();


        //Load destination photo.
        new GetDestinationImage(this, destination_code, ivPhoto).execute();

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
