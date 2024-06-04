package com.example.project.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.project.Domain.Barber;
import com.example.project.Helper;
import com.example.project.R;

import java.io.File;

public class BarberInfo extends AppCompatActivity {

    TextView name, name2, phoneNum, address;
    com.google.android.material.imageview.ShapeableImageView pic;
    Barber barber;
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
}