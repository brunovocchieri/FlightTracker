package com.example.brunovocchieri.klmflighttracker.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.brunovocchieri.klmflighttracker.Constants;
import com.example.brunovocchieri.klmflighttracker.Objects.Flight;
import com.example.brunovocchieri.klmflighttracker.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bruno Vocchieri on 23/10/2016.
 */public class ListFlightsAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    Context mContext;
    ArrayList<Flight> flights;

    public ListFlightsAdapter(Activity mContext, ArrayList<Flight> flights) {
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.flights = flights;
    }

    static class ViewHolder{
        @Bind(R.id.tvFlightNumber)   TextView tvFlightNumber;
        @Bind(R.id.tvOrigin)         TextView tvOrigin;
        @Bind(R.id.tvDestination)    TextView tvDestination;
        @Bind(R.id.tvDepartureTime)  TextView tvDepartureTime;
        @Bind(R.id.tvArrivalTime)    TextView tvArrivalTime;
        @Bind(R.id.tvStatus)         TextView tvStatus;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getCount() {
        return flights.size();
    }

    @Override
    public Object getItem(int position) {
        return flights.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        }
        else {
            convertView = mInflater.inflate(R.layout.flights_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        Flight f = flights.get(position);

        holder.tvFlightNumber.setText(f.getFlightNumber());
        holder.tvOrigin.setText(f.getOperatingFlightLeg().getDepartsFrom().getIATACode());
        holder.tvDestination.setText(f.getOperatingFlightLeg().getArrivesOn().getIATACode());

        holder.tvDepartureTime.setText(Constants.formatDate(f.getOperatingFlightLeg().getScheduledDepartureDateTime()));
        holder.tvArrivalTime.setText(Constants.formatDate(f.getOperatingFlightLeg().getScheduledArrivalDateTime()));

        holder.tvStatus.setText(f.getOperatingFlightLeg().getFlightStatus().replace("_", " "));


        return convertView;

    }
}

