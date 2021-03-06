package com.carty;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noman on 4/25/2016.
 */
public class ListFragment extends Fragment {

    private ArrayList<Truck> trucks = new ArrayList<>();
    private RecyclerView rv;
    private TruckListAdapter adapter;
    String foodType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        foodType = getArguments().getString("FoodType");
        initializeData();
        View view = inflater.inflate(R.layout.activity_list, container, false);
        rv = (RecyclerView)view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        return view;
    }
    
    // initialize data for food trucks
    private void initializeData() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Foodtrucks");
        query.whereEqualTo("Type", foodType);
        query.whereEqualTo("AddToTotal", true);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects) {
                        String name = object.getString("Name");
                        Log.e("LIST NAME: ", name);
                        ParseGeoPoint point = object.getParseGeoPoint("Location");
                        String type = object.getString("Type");
                        addToList(name, point, type);
                    }
                    adapter = new TruckListAdapter(trucks);
                    rv.setAdapter(adapter);
                } else {
                    Toast parseError = Toast.makeText(getContext(), "There was a Parse Error", Toast.LENGTH_SHORT);
                    parseError.show();
                }
            }
        });
    }

    private void addToList(String name, ParseGeoPoint point, String type) {
        trucks.add(new Truck(name, type, point));
    }
}
