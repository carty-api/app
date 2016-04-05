package com.carty;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("Main", "In main activity");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        welcome = (TextView) findViewById(R.id.welcome);
        welcome.append("\n");
        setSupportActionBar(toolbar);
        getLocations();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getLocations() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Foodtrucks");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects) {
                        String name = object.getString("Name");
                        Log.e("Name: ", name);
                        ParseGeoPoint point = object.getParseGeoPoint("Location");
                        Log.e("Point: ", point.toString());
                        String type = object.getString("Type");
                        Log.e("Type: ", type);
                        addToText(name, point, type);
                    }
                }
                else {
                    Log.e("Parse error", e.getMessage());
                }
            }
        });
    }

    private void addToText(String name, ParseGeoPoint point, String type) {
        welcome.append("Name: " + name);
        welcome.append("\n");
        welcome.append(point.toString());
        welcome.append("\n");
        welcome.append("Type: " + type);
        welcome.append("\n");
        welcome.append("\n");
    }
}
