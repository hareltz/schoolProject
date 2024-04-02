package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Adapter.AppointmentAdapter;
import com.example.project.Adapter.BarberAdapter;
import com.example.project.Decoration.ItemSpacingDecorationRight;
import com.example.project.Domain.Appointment;
import com.example.project.Domain.Barber;
import com.example.project.R;

import java.util.ArrayList;

public class MainPage extends AppCompatActivity {

    private RecyclerView appointments,favourites, populars;
    private RecyclerView.Adapter appointments_add,favourites_add, populars_add;
    EditText searchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        appointments = findViewById(R.id.appointment_list);
        favourites = findViewById(R.id.favourites_list);
        populars = findViewById(R.id.popular_list);
        searchBar = findViewById(R.id.search_bar);

        initAppointment();
        initFavourites();
        initPopular();

        // Set the OnEditorActionListener inside onCreate method
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            // function that detects when the user press enter on the search_bar EditText
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    EnterSearchResults();
                    return true;
                }
                return false;
            }
        });
    }

    private void EnterSearchResults()
    {
        Intent intent = new Intent(this, SearchResultsActivity.class); // run the main class
        Log.d("debug", searchBar.getText().toString());
        intent.putExtra("searchKey", searchBar.getText().toString()); // Example: sending a string value
        startActivity(intent);
        finish();
    }

    private void initAppointment()
    {
        // replace this with data from the db
        ArrayList<Appointment> appointments = new ArrayList<>();

        Barber tempBarber = new Barber("harel", "050-7870003", R.drawable.user_1);
        appointments.add(new Appointment(tempBarber, "01/04/2024", "14:00"));

        tempBarber = new Barber("harel2", "050-7870003", R.drawable.user_1);
        appointments.add(new Appointment(tempBarber, "02/04/2024", "12:00"));

        tempBarber = new Barber("harel3", "050-7870003", R.drawable.user_1);
        appointments.add(new Appointment(tempBarber, "03/04/2024", "16:00"));

        tempBarber = new Barber("harel4", "050-7870003", R.drawable.user_1);
        appointments.add(new Appointment(tempBarber, "03/04/2024", "18:00"));
        Log.d("MainActivity", "This is a debug message");
        // Initialize RecyclerView and set layout manager
        this.appointments = findViewById(R.id.appointment_list);
        this.appointments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Create and set adapter
        appointments_add = new AppointmentAdapter(appointments);
        this.appointments.setAdapter(appointments_add);

        // Apply ItemSpacingDecoration to add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        this.appointments.addItemDecoration(new ItemSpacingDecorationRight(this, spacingInPixels));
    }

    private void initFavourites()
    {
        ArrayList<Barber> barbers = new ArrayList<>();
        barbers.add(new Barber("harel", "050-7870003", R.drawable.user_1));
        barbers.add(new Barber("harel2", "050-7870003", R.drawable.user_1));
        barbers.add(new Barber("harel3", "050-7870003", R.drawable.user_1));
        barbers.add(new Barber("harel4", "050-7870003", R.drawable.user_1));

        // Initialize RecyclerView and set layout manager
        favourites = findViewById(R.id.favourites_list);
        favourites.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Create and set adapter
        favourites_add = new BarberAdapter(barbers);
        favourites.setAdapter(favourites_add);

        // Apply ItemSpacingDecoration to add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        favourites.addItemDecoration(new ItemSpacingDecorationRight(this, spacingInPixels));
    }

    private void initPopular()
    {
        ArrayList<Barber> barbers = new ArrayList<>();
        barbers.add(new Barber("harel", "050-7870003", R.drawable.user_1));
        barbers.add(new Barber("harel2", "050-7870003", R.drawable.user_1));
        barbers.add(new Barber("harel3", "050-7870003", R.drawable.user_1));
        barbers.add(new Barber("harel4", "050-7870003", R.drawable.user_1));

        // Initialize RecyclerView and set layout manager
        populars = findViewById(R.id.popular_list);
        populars.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Create and set adapter
        populars_add = new BarberAdapter(barbers);
        populars.setAdapter(populars_add);

        // Apply ItemSpacingDecoration to add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        populars.addItemDecoration(new ItemSpacingDecorationRight(this, spacingInPixels));
    }



}