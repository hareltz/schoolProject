package com.example.project.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.Domain.Barber;
import com.example.project.Helper;
import com.example.project.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class BarberInfo extends AppCompatActivity {

    TextView name, name2, phoneNum, address;
    Barber barber_;
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

        Intent intent = getIntent();

        String barberId = intent.getStringExtra("barberId");
        Barber barber = Helper.getBarberDataById(barberId);

        name.setText(barber.getName());
        name2.setText(barber.getName());
        phoneNum.setText(barber.getPhone_number());
        //address.setText(intent.getStringExtra(getAddressFromGeoPoint(barber.getLocation())));

    }

    private String getAddressFromGeoPoint(GeoPoint geoPoint) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder addressString = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressString.append(address.getAddressLine(i)).append("\n");
                }
                return addressString.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    public void MakeAppointment(View view)
    {

        Intent intent = new Intent(this, AppointmentActivity.class); // run the main class
        intent.putExtra("nameKey", name.getText());
        intent.putExtra("picAddKey", "user_1");

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