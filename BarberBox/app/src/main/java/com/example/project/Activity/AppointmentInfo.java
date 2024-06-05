package com.example.project.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.project.Domain.Appointment;
import com.example.project.Domain.Barber;
import com.example.project.Helper;
import com.example.project.R;

import java.io.File;
import java.util.Objects;

public class AppointmentInfo extends AppCompatActivity {
    TextView name, name2, phoneNum, address, date;
    AlertDialog.Builder builder;
    com.google.android.material.imageview.ShapeableImageView pic;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        builder = new AlertDialog.Builder(this);

        // change this to get only the Appointment ID and get this info from the DB

        name = findViewById(R.id.appointmentInfo_name);
        name2 = findViewById(R.id.appointmentInfo_name2);
        phoneNum = findViewById(R.id.appointmentInfo_phone_number);
        address = findViewById(R.id.appointmentInfo_location);
        date = findViewById(R.id.appointmentInfo_date);
        pic = findViewById(R.id.appointmentInfo_pic);

        Intent intent = getIntent();
        String barberId = intent.getStringExtra("barberId");
        String appointmentDate = intent.getStringExtra("appointmentDate");

        Barber barber = Helper.getBarberDataById(barberId);
        Appointment appointment = new Appointment();

        for (Appointment tempAppointment : barber.getAppointments())
        {
            if (Objects.equals(tempAppointment.getDate(), appointmentDate))
            {
                appointment = tempAppointment;
            }
        }

        name.setText(barber.getName());
        name2.setText(barber.getName());
        phoneNum.setText(barber.getPhone_number());
        address.setText(Helper.getAddressFromGeoPoint(barber.getLocation(), this));
        date.setText(Helper.getDateFromTimestamp(appointment.getTime()) + " - " + Helper.getTimeFromTimestamp(appointment.getTime()));
        File localFile = Helper.getImageFile(barber.getName().replace(" ", "_") + ".png");

        if (localFile.exists())
        {
            Glide.with(this)
                    .load(localFile)
                    .into(pic);
        }
    }

    @SuppressLint("SetTextI18n")
    public void CancelAppointment(View view)
    {
        builder.setTitle("Alert!")
                .setMessage("Do you sure want to cancel the appointment?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AppointmentInfo.this, MainPage.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void ArrowBack(View view)
    {
        Intent intent = new Intent(this, MainPage.class); // run the main class
        startActivity(intent);
        finish();
    }
}