package com.example.project.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.Objects;

public class AppointmentInfo extends AppCompatActivity {
    TextView name, name2, phoneNum, address, date;
    AlertDialog.Builder builder;
    com.google.android.material.imageview.ShapeableImageView pic;
    private Barber barber = new Barber();
    private Appointment appointment = new Appointment();
    String appointmentDocumentName;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
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
        appointmentDocumentName = intent.getStringExtra("appointmentDocumentName");

        barber = Helper.getBarberDataById(barberId);
        appointment = new Appointment();

        for (Appointment tempAppointment : barber.getAppointments())
        {
            if (Objects.equals(tempAppointment.getDocumentName(), appointmentDocumentName))
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
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // Get the barber document reference by barber name
                        DocumentReference barberRef = FirebaseFirestore.getInstance().collection("barbers").document(barber.get_id());

                        // Get the appointment document reference by timestamp under the barber's appointments collection
                        DocumentReference appointmentRef = barberRef.collection("appointments").document(appointment.getDocumentName());

                        appointmentRef
                                .update("user_id", "")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        Toast.makeText(AppointmentInfo.this, "The appointment was successfully cancelled!", Toast.LENGTH_SHORT).show();

                                        for (Appointment tempAppointment : barber.getAppointments())
                                        {
                                            if (Objects.equals(tempAppointment.getDocumentName(), appointmentDocumentName))
                                            {
                                                tempAppointment.setUser_id("");
                                                break;
                                            }
                                        }
                                        barber.changeAppointmentUserId(appointmentDocumentName);

                                        for (Appointment a : Helper.appointments)
                                        {
                                            if (Objects.equals(a.getDocumentName(), appointmentDocumentName))
                                            {
                                                Helper.appointments.remove(a);
                                                Intent intent = new Intent(AppointmentInfo.this, MainPage.class);
                                                startActivity(intent);
                                                finish();
                                                break;
                                            }
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AppointmentInfo.this, "Problem canceling the appointment, contact support", Toast.LENGTH_SHORT).show();
                                    }
                                });


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

    // this function open the location on google maps
    public void locationClick(View view)
    {
        double latitude = appointment.getBarber().getLocation().getLatitude();
        double longitude = appointment.getBarber().getLocation().getLongitude();

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
}