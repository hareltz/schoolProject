package com.example.project.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.project.Adapter.AppointmentAdapter;
import com.example.project.Adapter.BarberAdapter;
import com.example.project.Decoration.ItemSpacingDecoration;
import com.example.project.Domain.Appointment;
import com.example.project.Domain.Barber;
import com.example.project.R;

import java.util.ArrayList;

public class MainPage extends AppCompatActivity {

    private RecyclerView appointments,favourites, populars;
    private RecyclerView.Adapter appointments_add,favourites_add, populars_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        appointments = findViewById(R.id.appointment_list);
        favourites = findViewById(R.id.favourites_list);
        populars = findViewById(R.id.popular_list);

        initAppointment();
        initFavourites();
        initPopular();
    }

    private void initAppointment()
    {
        // replace this with data from the db
        ArrayList<Appointment> appointments = new ArrayList<>();

        Barber tempBarber = new Barber("harel", "050-7870003", "@drawable/user_2");
        appointments.add(new Appointment(tempBarber, "01/04/2024", "14:00"));

        tempBarber = new Barber("harel2", "050-7870003", "@drawable/user_2");
        appointments.add(new Appointment(tempBarber, "02/04/2024", "12:00"));

        tempBarber = new Barber("harel3", "050-7870003", "@drawable/user_2");
        appointments.add(new Appointment(tempBarber, "03/04/2024", "16:00"));

        tempBarber = new Barber("harel4", "050-7870003", "@drawable/user_2");
        appointments.add(new Appointment(tempBarber, "03/04/2024", "18:00"));
        Log.d("MainActivity", "This is a debug message");
        // Initialize RecyclerView and set layout manager
        this.appointments = findViewById(R.id.appointment_list);
        this.appointments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Log.d("MainActivity", "This is a debug message");
        // Create and set adapter
        appointments_add = new AppointmentAdapter(appointments);
        this.appointments.setAdapter(appointments_add);
        Log.d("MainActivity", "This is a debug message");
        // Apply ItemSpacingDecoration to add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        this.appointments.addItemDecoration(new ItemSpacingDecoration(this, spacingInPixels));
        Log.d("MainActivity", "This is a debug message");
    }

    private void initFavourites()
    {
        ArrayList<Barber> barbers = new ArrayList<>();
        barbers.add(new Barber("harel", "050-7870003", "@drawable/user_2"));
        barbers.add(new Barber("harel2", "050-7870003", "@drawable/user_2"));
        barbers.add(new Barber("harel3", "050-7870003", "@drawable/user_2"));
        barbers.add(new Barber("harel4", "050-7870003", "@drawable/user_2"));

        // Initialize RecyclerView and set layout manager
        favourites = findViewById(R.id.favourites_list);
        favourites.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Create and set adapter
        favourites_add = new BarberAdapter(barbers);
        favourites.setAdapter(favourites_add);

        // Apply ItemSpacingDecoration to add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        favourites.addItemDecoration(new ItemSpacingDecoration(this, spacingInPixels));
    }

    private void initPopular()
    {
        ArrayList<Barber> barbers = new ArrayList<>();
        barbers.add(new Barber("harel", "050-7870003", "@drawable/user_2"));
        barbers.add(new Barber("harel2", "050-7870003", "@drawable/user_2"));
        barbers.add(new Barber("harel3", "050-7870003", "@drawable/user_2"));
        barbers.add(new Barber("harel4", "050-7870003", "@drawable/user_2"));

        // Initialize RecyclerView and set layout manager
        populars = findViewById(R.id.popular_list);
        populars.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Create and set adapter
        populars_add = new BarberAdapter(barbers);
        populars.setAdapter(populars_add);

        // Apply ItemSpacingDecoration to add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        populars.addItemDecoration(new ItemSpacingDecoration(this, spacingInPixels));
    }



}