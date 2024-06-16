package com.example.project.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.Adapter.AppointmentChooseAdapter;
import com.example.project.Decoration.ItemSpacingDecorationBottom;
import com.example.project.Domain.Appointment;
import com.example.project.Domain.Barber;
import com.example.project.Helper;
import com.example.project.Interfaces.IRecyclerViewOnAppointmentClick;
import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class AppointmentActivity extends AppCompatActivity implements IRecyclerViewOnAppointmentClick {

    private RecyclerView appointmentsRV;
    private RecyclerView.Adapter appointments_add;
    private TextView name;
    AlertDialog.Builder builder;

    com.google.android.material.imageview.ShapeableImageView pic;
    Barber barber;
    ArrayList<Appointment> appointments = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        builder = new AlertDialog.Builder(this);

        pic = findViewById(R.id.appointmentScreen_profile_pic);
        name = findViewById(R.id.appointmentScreen_BarberName);

        Intent intent = getIntent();

        String barberId = intent.getStringExtra("barberId");
        barber = Helper.getBarberDataById(barberId);
        name.setText(barber.getName());
        File localFile = Helper.getImageFile(barber.getName().replace(" ", "_") + ".png");

        if (localFile.exists())
        {
            Glide.with(this)
                    .load(localFile)
                    .into(pic);
        }

        initAppointment(barber);
        // get the data from the db (by barber ID)

    }

    @Override
    public void onAppointmentClick(int position) {
        builder.setTitle("Alert!")
                .setMessage("Are you sure you want to make an appointment?")
                .setCancelable(true)
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(AppointmentActivity.this, MainPage.class);
                    appointments.get(position).setUser_id(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    Helper.appointments.add(appointments.get(position));

                    // Get the barber document reference by barber name
                    DocumentReference barberRef = FirebaseFirestore.getInstance().collection("barbers").document(barber.get_id());

                    // Get the appointment document reference by timestamp under the barber's appointments collection
                    DocumentReference appointmentRef = barberRef.collection("appointments").document(appointments.get(position).getDocumentName());

                    appointmentRef
                            .update("user_id",  FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .addOnCompleteListener(task ->
                                    {
                                        Toast.makeText(AppointmentActivity.this, "The appointment was successfully made!", Toast.LENGTH_SHORT).show();
                                        Helper.setNotification(AppointmentActivity.this, appointments.get(position).getTime());
                                        Helper.addEventToCalendar(AppointmentActivity.this, appointments.get(position).getTime(), barber);
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(AppointmentActivity.this, "Appointment declined, contact support", Toast.LENGTH_SHORT).show());
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void initAppointment(Barber barber) // take here data from DB
    {
        // replace this with data from the db
        ArrayList<Appointment> tempAppointments = new ArrayList<>(barber.getAppointments());
        Timestamp currentTimestamp = Timestamp.now();

        // remove the taken appointments
        for (Appointment appointment : tempAppointments)
        {
            if (Objects.equals(appointment.getUser_id(), "") // check if the appointment in already taken
                    && appointment.getTime().compareTo(currentTimestamp) > 0) // check if the time of the appointment is in the future
            {
                this.appointments.add(appointment);
            }
        }

        tempAppointments.sort(new Helper.AppointmentTimeComparator());

        // Initialize RecyclerView and set layout manager
        this.appointmentsRV = findViewById(R.id.appointmentScreen_search_results_list);
        this.appointmentsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Create and set adapter
        appointments_add = new AppointmentChooseAdapter(this.appointments, this);
        this.appointmentsRV.setAdapter(appointments_add);

        // Apply ItemSpacingDecoration to add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        this.appointmentsRV.addItemDecoration(new ItemSpacingDecorationBottom(this, spacingInPixels));
    }

    public void ArrowBack(View view)
    {
        // go back to the main screen
        Intent intent = new Intent(this, MainPage.class); // run the main class
        startActivity(intent);
        finish();
    }
}