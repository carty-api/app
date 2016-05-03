package com.carty;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.ImagePickerSheetView;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by Noman on 4/7/2016.
 */
public class AddCartActivity extends AppCompatActivity {

    EditText yourName;
    EditText cartName;
    EditText cartType;
    //Button getLocationButton;
    Location userLoc;
    BottomSheetLayout bottomSheetLayout;
    ImageView selectedImage;
    byte[] image = null;
    ParseObject carts;

    //int MY_PERMISSIONS_REQUEST_STORAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);
        Intent i = getIntent();
        userLoc = i.getParcelableExtra("UserLocation");
        yourName = (EditText) findViewById(R.id.yourNameET);
        cartName = (EditText) findViewById(R.id.cartNameET);
        cartType = (EditText) findViewById(R.id.cartTypeET);
        selectedImage = (ImageView) findViewById(R.id.image_selected);
        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomSheet);
        carts = new ParseObject("Foodtrucks");
        findViewById(R.id.getLocationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userLoc == null) {
                    Toast.makeText(AddCartActivity.this, "Please enable your location services", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast currentLoc = Toast.makeText(getApplicationContext(), String.valueOf(userLoc.getLatitude()) + " , " + String.valueOf(userLoc.getLongitude()), Toast.LENGTH_SHORT);
                    currentLoc.show();
                }


            }
        });
        findViewById(R.id.image_picker_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //checkPerAndShow();
                showSheetView();
            }
        });
        selectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkPerAndShow();
                showSheetView();
            }
        });
        findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }
//
//    private void checkPerAndShow() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    MY_PERMISSIONS_REQUEST_STORAGE);
//        } else {
//            showSheetView();
//        }
//    }

    private void showSheetView() {
        ImagePickerSheetView sheetView = new ImagePickerSheetView.Builder(this)
                .setMaxItems(30)
                .setShowCameraOption(false)
                .setShowPickerOption(false)
                .setImageProvider(new ImagePickerSheetView.ImageProvider() {
                    @Override
                    public void onProvideImage(ImageView imageView, Uri imageUri, int size) {
                        Glide.with(AddCartActivity.this)
                                .load(imageUri)
                                .centerCrop()
                                .crossFade()
                                .into(imageView);
                    }
                })
                .setOnTileSelectedListener(new ImagePickerSheetView.OnTileSelectedListener() {
                    @Override
                    public void onTileSelected(ImagePickerSheetView.ImagePickerTile selectedTile) {
                        bottomSheetLayout.dismissSheet();
                        if (selectedTile.isImageTile()) {
                            showSelectedImage(selectedTile.getImageUri());
                        } else {
                            genericError(null);
                        }
                    }
                })
                .setTitle("Choose an image...")
                .create();

        bottomSheetLayout.showWithSheetView(sheetView);
    }

    private void showSelectedImage(Uri selectedImageUri) {
        selectedImage.setImageDrawable(null);
        Glide.with(this)
                .load(selectedImageUri)
                .crossFade()
                .fitCenter()
                .into(selectedImage);
        convertImage(selectedImageUri);
        //Log.e("File size ", "is " + image.length);
    }

    private void genericError(String message) {
        Toast.makeText(this, message == null ? "Something went wrong." : message, Toast.LENGTH_SHORT).show();
    }

    private void convertImage(Uri imageUri) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bmpSample = BitmapFactory.decodeFile(imageUri.getPath(), options);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bmpSample.compress(Bitmap.CompressFormat.JPEG, 100, out);
            image = out.toByteArray();
//            ParseFile file = new ParseFile(yourName.getText().toString() + cartName.getText().toString() + ".png", image);
//            file.saveInBackground(new SaveCallback() {
//                @Override
//                public void done(ParseException e) {
//                    if (e == null) {
//                        saveToParse();
//                    }
//                    else {
//                        Toast.makeText(AddCartActivity.this, "File upload didn't work", Toast.LENGTH_SHORT).show();
//                        Log.e("SAVE ERROR", e.getMessage());
//                    }
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void submit() {
        String userName = yourName.getText().toString();
        String cartN = cartName.getText().toString();
        //String cartLoc = cartLocation.getText().toString();
        String cartT = cartType.getText().toString();
        ParseGeoPoint point = new ParseGeoPoint();

        if (userLoc == null) {
            Toast.makeText(AddCartActivity.this, "Your location could not be determined", Toast.LENGTH_SHORT).show();
        }
        else {
            point.setLatitude(userLoc.getLatitude());
            point.setLongitude(userLoc.getLongitude());
        }
        carts.put("User_name", userName);
        carts.put("Cart_name", cartN);
        carts.put("Location", point);
        carts.put("Type", cartT);
        carts.put("AddToTotal", false);
        carts.saveInBackground();

        Toast submitted = Toast.makeText(getApplicationContext(), "Thank you for your submission", Toast.LENGTH_SHORT);
        submitted.show();
        startActivity(new Intent(AddCartActivity.this, MainActivity.class));
    }

//    private void saveToParse() {
//        carts.put("Picture", image);
//    }
}
