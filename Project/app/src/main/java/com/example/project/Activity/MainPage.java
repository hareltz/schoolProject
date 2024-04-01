package com.example.project.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.project.Adapter.BarberAdapter;
import com.example.project.Decoration.ItemSpacingDecoration;
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
    }

    private void initAppointment() {
        ArrayList<Barber> barbers = new ArrayList<>();
        barbers.add(new Barber("harel", "050-7870003", "@drawable/user_2"));
        barbers.add(new Barber("harel2", "050-7870003", "@drawable/user_2"));
        barbers.add(new Barber("harel3", "050-7870003", "@drawable/user_2"));
        barbers.add(new Barber("harel4", "050-7870003", "@drawable/user_2"));

        // Initialize RecyclerView and set layout manager
        appointments = findViewById(R.id.appointment_list);
        appointments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Create and set adapter
        appointments_add = new BarberAdapter(barbers);
        appointments.setAdapter(appointments_add);

        // Apply ItemSpacingDecoration to add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        appointments.addItemDecoration(new ItemSpacingDecoration(this, spacingInPixels));
    }



}