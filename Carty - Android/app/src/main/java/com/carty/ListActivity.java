package com.carty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noman on 4/18/2016.
 */
public class ListActivity extends AppCompatActivity {
    private ArrayList<Truck> trucks = new ArrayList<>();
    private RecyclerView rv;
    private TruckListAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        setContentView(R.layout.activity_list);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);

    }

    private void getData() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Foodtrucks");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects) {
                        String name = object.getString("Name");
                        ParseGeoPoint point = object.getParseGeoPoint("Location");
                        String type = object.getString("Type");
                        addToList(name, point, type);
                    }
                    adapter = new TruckListAdapter(trucks);
                    rv.setAdapter(adapter);
                } else {
                    Toast parseError = Toast.makeText(getApplicationContext(), "There was a Parse Error", Toast.LENGTH_SHORT);
                    parseError.show();
                }
            }
        });
    }

    private void addToList(String name, ParseGeoPoint point, String type) {
        trucks.add(new Truck(name, type, point));
    }
}
