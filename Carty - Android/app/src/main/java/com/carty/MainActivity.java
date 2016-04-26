package com.carty;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity
        implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mClient = null;
    Location mLocation;
    CardView halalCard;
    CardView coffeeCard;
    CardView otherCard;
    int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        halalCard = (CardView) findViewById(R.id.halalCV);
        coffeeCard = (CardView) findViewById(R.id.coffeeCV);
        otherCard = (CardView) findViewById(R.id.otherCV);
        if (mClient == null) {
            mClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        //getLocations();
        setListeners();
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

//    private void getLocations() {
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Foodtrucks");
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> objects, ParseException e) {
//                if (e == null) {
//                    for (ParseObject object : objects) {
//                        String name = object.getString("Name");
//                        //Log.e("Name: ", name);
//                        ParseGeoPoint point = object.getParseGeoPoint("Location");
//                        //Log.e("Point: ", point.toString());
//                        String type = object.getString("Type");
//                        //Log.e("Type: ", type);
//                        addToText(name, point, type);
//                    }
//                } else {
//                    Log.e("Parse error", e.getMessage());
//                }
//            }
//        });
//    }

//    private void addToText(String name, ParseGeoPoint point, String type) {
//        welcome.append("Name: " + name);
//        welcome.append("\n");
//        welcome.append(point.toString());
//        welcome.append("\n");
//        welcome.append("Type: " + type);
//        welcome.append("\n");
//        welcome.append("\n");
//    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {

            getLoc();
        }
    }

    private void getLoc() {
        mLocation = LocationServices.FusedLocationApi.getLastLocation(
                mClient);
        if (mLocation != null) {
            Log.e("My Latitude", (String.valueOf(mLocation.getLatitude())));
            Log.e("My Long", String.valueOf(mLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        mClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mClient.disconnect();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLoc();
            }
        }
    }

    private void setListeners() {

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addCart = new Intent(MainActivity.this, AddCartActivity.class);
                addCart.putExtra("UserLocation", mLocation);
                startActivity(addCart);
            }
        });

        halalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast submitted = Toast.makeText(getApplicationContext(), "Halal Toast", Toast.LENGTH_SHORT);
                submitted.show();
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        });


        coffeeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast submitted = Toast.makeText(getApplicationContext(), "Coffee Toast", Toast.LENGTH_SHORT);
                submitted.show();
                startActivity(new Intent(MainActivity.this, TabActivity.class));

            }
        });

        otherCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast submitted = Toast.makeText(getApplicationContext(), "Other Toast", Toast.LENGTH_SHORT);
                submitted.show();
                startActivity(new Intent(MainActivity.this, ListActivity.class));

            }
        });
    }
}
