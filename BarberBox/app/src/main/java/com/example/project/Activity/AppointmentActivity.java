package com.example.project.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Adapter.AppointmentChooseAdapter;
import com.example.project.Decoration.ItemSpacingDecorationBottom;
import com.example.project.Domain.Appointment;
import com.example.project.Domain.Barber;
import com.example.project.Interfaces.IRecyclerViewOnAppointmentClick;
import com.example.project.R;

import java.util.ArrayList;

public class AppointmentActivity extends AppCompatActivity implements IRecyclerViewOnAppointmentClick {

    private RecyclerView appointments;
    private RecyclerView.Adapter appointments_add;
    private TextView name;
    AlertDialog.Builder builder;

    com.google.android.material.imageview.ShapeableImageView Pic;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        builder = new AlertDialog.Builder(this);

        Pic = findViewById(R.id.appointmentScreen_profile_pic);
        name = findViewById(R.id.appointmentScreen_BarberName);

        Intent intent = getIntent();
        name.setText(intent.getStringExtra("nameKey"));
        // put photo somehow

        initAppointment();
        // get the data from the db (by barber ID)

    }

    @Override
    public void onAppointmentClick(int position) {
        builder.setTitle("Alert!")
                .setMessage("Are you sure you want to make an appointment?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AppointmentActivity.this, MainPage.class);
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

    private void initAppointment() // take here data from DB
    {
        // replace this with data from the db
        ArrayList<Appointment> appointments = new ArrayList<>();

        Barber tempBarber = new Barber("harel", "050-7870003", R.drawable.user_1, "Yish'i 10");
        appointments.add(new Appointment(tempBarber, "01/04/2024", "14:00"));

        tempBarber = new Barber("harel2", "050-7870003", R.drawable.user_1, "Yish'i 10");
        appointments.add(new Appointment(tempBarber, "02/04/2024", "12:00"));

        tempBarber = new Barber("harel3", "050-7870003", R.drawable.user_1, "Yish'i 10");
        appointments.add(new Appointment(tempBarber, "03/04/2024", "16:00"));

        tempBarber = new Barber("harel4", "050-7870003", R.drawable.user_1, "Yish'i 10");
        appointments.add(new Appointment(tempBarber, "03/04/2024", "18:00"));
        Log.d("MainActivity", "This is a debug message");
        // Initialize RecyclerView and set layout manager
        this.appointments = findViewById(R.id.appointmentScreen_search_results_list);
        this.appointments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Create and set adapter
        appointments_add = new AppointmentChooseAdapter(appointments, this);
        this.appointments.setAdapter(appointments_add);

        // Apply ItemSpacingDecoration to add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        this.appointments.addItemDecoration(new ItemSpacingDecorationBottom(this, spacingInPixels));
    }

    public void ArrowBack(View view)
    {
        // go back to the main screen
        Intent intent = new Intent(this, MainPage.class); // run the main class
        startActivity(intent);
        finish();
    }
}