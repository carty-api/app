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
    EditText cartLocation;
    EditText cartType;
    //Button getLocationButton;
    Location userLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);
        Intent i = getIntent();
        userLoc = i.getParcelableExtra("UserLocation");
        yourName = (EditText) findViewById(R.id.yourNameET);
        cartName = (EditText) findViewById(R.id.cartNameET);
        cartLocation = (EditText) findViewById(R.id.cartLocationET);
        cartType = (EditText) findViewById(R.id.cartTypeET);
        findViewById(R.id.getLocationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartLocation.setText(String.valueOf(userLoc.getLatitude()) + " , " + String.valueOf(userLoc.getLongitude()));
            }
        });
        findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

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
