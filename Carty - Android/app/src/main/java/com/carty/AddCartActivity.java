package com.carty;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

/**
 * Created by Noman on 4/7/2016.
 */
public class AddCartActivity extends AppCompatActivity {

    EditText yourName;
    EditText cartName;
    EditText cartType;
    //Button getLocationButton;
    Location userLoc;

    // check if location services are turned on, else prompt the user to do so
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);
        Intent i = getIntent();
        userLoc = i.getParcelableExtra("UserLocation");
        yourName = (EditText) findViewById(R.id.yourNameET);
        cartName = (EditText) findViewById(R.id.cartNameET);
        cartType = (EditText) findViewById(R.id.cartTypeET);
        findViewById(R.id.getLocationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userLoc == null) {
                    Toast.makeText(AddCartActivity.this, "Please turn on your location services", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast currentLoc = Toast.makeText(getApplicationContext(), String.valueOf(userLoc.getLatitude()) + " , " + String.valueOf(userLoc.getLongitude()), Toast.LENGTH_SHORT);
                    currentLoc.show();
                }


            }
        });
        findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    // get details from user and send a parse request to store the data
    private void submit() {
        String userName = yourName.getText().toString();
        String cartN = cartName.getText().toString();
        //String cartLoc = cartLocation.getText().toString();
        String cartT = cartType.getText().toString();

        ParseObject carts = new ParseObject("AddedTrucks");
        ParseGeoPoint point = new ParseGeoPoint(userLoc.getLatitude(), userLoc.getLongitude());
        carts.put("User_name", userName);
        carts.put("Cart_name", cartN);
        carts.put("Location", point);
        carts.put("Type", cartT);

        carts.saveInBackground();

        Toast submitted = Toast.makeText(getApplicationContext(), "Thank you for your submission", Toast.LENGTH_SHORT);
        submitted.show();


        startActivity(new Intent(AddCartActivity.this, MainActivity.class));
    }
}
