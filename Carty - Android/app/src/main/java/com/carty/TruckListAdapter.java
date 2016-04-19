package com.carty;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Noman on 4/18/2016.
 */
public class TruckListAdapter extends RecyclerView.Adapter<TruckListAdapter.TruckListViewHolder> {

    private ArrayList<Truck> trucks;

    public TruckListAdapter(ArrayList<Truck> sites) {
        this.trucks = sites;
    }

    @Override
    public int getItemCount() {
        return trucks.size();
    }

    @Override
    public TruckListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext())
                .inflate(R.layout.item_in_list, parent, false);

        return new TruckListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TruckListViewHolder holder, int position) {
        Truck t = trucks.get(position);
        holder.truckName.setText(t.getName());
        holder.truckType.setText(t.getType());
        holder.truckLoc.setText("Location: " + String.valueOf(t.getLocation().getLatitude()) + ", "
                + String.valueOf(t.getLocation().getLongitude()));
    }

    public static class TruckListViewHolder extends RecyclerView.ViewHolder {

        protected TextView truckName;
        protected TextView truckType;
        protected TextView truckLoc;

        public TruckListViewHolder(View itemView) {
            super(itemView);
            truckName = (TextView) itemView.findViewById(R.id.truck_name);
            truckType = (TextView) itemView.findViewById(R.id.truck_type);
            truckLoc = (TextView) itemView.findViewById(R.id.truck_location);
        }
    }
}
