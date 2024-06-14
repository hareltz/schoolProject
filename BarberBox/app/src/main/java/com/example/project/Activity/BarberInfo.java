package com.example.project.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.project.Domain.Barber;
import com.example.project.Helper;
import com.example.project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BarberInfo extends AppCompatActivity {

    TextView name, name2, phoneNum, address;
    com.google.android.material.imageview.ShapeableImageView pic;
    Barber barber;
    ToggleButton likeBt;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_barber_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // change this to get only the barber ID and get this info from the DB

        name = findViewById(R.id.barberInfo_name);
        name2 = findViewById(R.id.barberInfo_name2);
        phoneNum = findViewById(R.id.barberInfo_phone_number);
        address = findViewById(R.id.barberInfo_location);
        pic = findViewById(R.id.barberInfo_profile_pic);
        likeBt = findViewById(R.id.barberInfo_likeBt);

        Intent intent = getIntent();

        String barberId = intent.getStringExtra("barberId");
        barber = Helper.getBarberDataById(barberId);

        name.setText(barber.getName());
        name2.setText(barber.getName());
        phoneNum.setText(barber.getPhone_number());
        address.setText(Helper.getAddressFromGeoPoint(barber.getLocation(), this)); // let the user press the location for map
        File localFile = Helper.getImageFile(barber.getName().replace(" ", "_") + ".png");

        if (localFile.exists())
        {
            Glide.with(this)
                    .load(localFile)
                    .into(pic);
        }

        if (Helper.favourites.containsKey(barber.get_id()))
        {
            likeBt.setChecked(Boolean.TRUE.equals(Helper.favourites.get(barber.get_id())));
        }
    }

    @SuppressLint("SetTextI18n")
    public void MakeAppointment(View view)
    {
        Intent intent = new Intent(this, AppointmentActivity.class); // run the main class
        intent.putExtra("barberId", barber.get_id());
        startActivity(intent);
        finish();
    }

    public void ArrowBack(View view)
    {
        Intent intent = new Intent(this, MainPage.class); // run the main class
        startActivity(intent);
        finish();
    }

    // this function open the location on google maps
    public void locationClick(View view)
    {
        double latitude = barber.getLocation().getLatitude();
        double longitude = barber.getLocation().getLongitude();

        // URI -> Indicates the protocol to be used to access the resource (e.g., http, https, ftp, mailto, file, geo).
        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude); // create Uniform Resource Identifier
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps"); // Set the package to ensure the Intent is handled by Google Maps app
        if (mapIntent.resolveActivity(getPackageManager()) != null) // Check if there is an activity that can handle this Intent
        {
            // Start the activity to open Google Maps with the specified location
            startActivity(mapIntent);
        }

    }

    public void likeOnClick(View view)
    {
        DocumentReference barberRef = FirebaseFirestore.getInstance().collection("favourites").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        if (likeBt.isChecked())
        {
            barberRef.update(barber.get_id(), true);
            Helper.favourites.put(barber.get_id(), true);
        }
        else
        {
            barberRef.update(barber.get_id(), false);
            Helper.favourites.put(barber.get_id(), false);
        }
    }
}