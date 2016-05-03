package com.carty;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Noman on 4/25/2016
 */
public class MapFragment extends Fragment
        implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    MapView mMapView;
    private int pixelPadding;
    private GoogleMap googleMap;
    private Location mLocation;
    int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    GoogleApiClient mClient = null;
    String foodType;


    public MapFragment(){
        //required empty
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mClient == null) {
            mClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_map_view, container,
                false);
        pixelPadding = getArguments().getInt("Pixels");
        foodType = getArguments().getString("FoodType");
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        googleMap = mMapView.getMap();

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        //Log.i("FragmentHeight", Integer.toString(pixelPadding));
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            googleMap.setMyLocationEnabled(true);
            getLoc();
            if (mLocation != null) {
                LatLng current = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 14));

            }
            else {
                LatLng brooklyn = new LatLng(40.6944462,-73.9877854);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(brooklyn, 14));
            }
        }

        //}
        googleMap.setPadding(0, 0, 0, pixelPadding);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Foodtrucks");
        query.whereEqualTo("Type", foodType);
        query.whereEqualTo("AddToTotal", true);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects) {
                        String name = object.getString("Name");
                        ParseGeoPoint point = object.getParseGeoPoint("Location");
                        String type = object.getString("Type");
                        drawMarker(point, name, type);
                    }
                } else {
                    Toast parseError = Toast.makeText(getContext(), "There was a Parse Error", Toast.LENGTH_SHORT);
                    parseError.show();
                }
            }
        });

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                //TODO: Any custom actions
                if (mLocation != null) {
                    LatLng cur = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                    //drawMarker(cur, "user's location", "");
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cur, 14));
                    return true;
                } else {
                    //Toast.makeText(getContext(), "Your location could not be determined", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

        });


        return v;
    }


    private void drawMarker(ParseGeoPoint point, String title, String snippet) {
        LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().title(title).snippet(snippet).position(latLng);
        googleMap.addMarker(markerOptions);
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
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


}
