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

import com.example.project.R;

public class AppointmentInfo extends AppCompatActivity {
    TextView name, name2, phoneNum, address, date;
    AlertDialog.Builder builder;

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

        Intent intent = getIntent();
        String barberId = intent.getStringExtra("barberId");
        String appointmentId = intent.getStringExtra("appointmentDate");

       /* name.setText();
        name2.setText();
        phoneNum.setText();
        address.setText();
        date.setText();*/
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