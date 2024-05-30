package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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
import com.example.project.Interfaces.IRecyclerViewOnAppointmentClick;
import com.example.project.Interfaces.IRecyclerViewOnBarberClick;
import com.example.project.R;

import java.util.ArrayList;

public class MainPage extends AppCompatActivity implements IRecyclerViewOnAppointmentClick, IRecyclerViewOnBarberClick {

    private RecyclerView appointments,favourites, populars;
    private RecyclerView.Adapter appointments_add, favourites_add, populars_add;
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
        this.appointments = findViewById(R.id.appointment_list);
        this.appointments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Create and set adapter
        appointments_add = new AppointmentAdapter(appointments, this);
        this.appointments.setAdapter(appointments_add);

        // Apply ItemSpacingDecoration to add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        this.appointments.addItemDecoration(new ItemSpacingDecorationRight(this, spacingInPixels));
    }

    private void initFavourites()
    {
        ArrayList<Barber> barbers = new ArrayList<>();
        barbers.add(new Barber("harel", "050-7870003", R.drawable.user_1, "Yish'i 10"));
        barbers.add(new Barber("harel2", "050-7870003", R.drawable.user_1, "Yish'i 10"));
        barbers.add(new Barber("harel3", "050-7870003", R.drawable.user_1, "Yish'i 10"));
        barbers.add(new Barber("harel4", "050-7870003", R.drawable.user_1, "Yish'i 10"));

        // Initialize RecyclerView and set layout manager
        favourites = findViewById(R.id.favourites_list);
        favourites.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Create and set adapter
        favourites_add = new BarberAdapter(barbers, this , 1);
        favourites.setAdapter(favourites_add);

        // Apply ItemSpacingDecoration to add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        favourites.addItemDecoration(new ItemSpacingDecorationRight(this, spacingInPixels));
    }

    private void initPopular()
    {
        ArrayList<Barber> barbers = new ArrayList<>();
        barbers.add(new Barber("harel", "050-7870003", R.drawable.user_1, "Yish'i 10"));
        barbers.add(new Barber("harel2", "050-7870003", R.drawable.user_1, "Yish'i 10"));
        barbers.add(new Barber("harel3", "050-7870003", R.drawable.user_1, "Yish'i 10"));
        barbers.add(new Barber("harel4", "050-7870003", R.drawable.user_1, "Yish'i 10"));

        // Initialize RecyclerView and set layout manager
        populars = findViewById(R.id.popular_list);
        populars.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Create and set adapter
        populars_add = new BarberAdapter(barbers, this , 2);
        populars.setAdapter(populars_add);

        // Apply ItemSpacingDecoration to add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        populars.addItemDecoration(new ItemSpacingDecorationRight(this, spacingInPixels));
    }


    @Override
    public void onAppointmentClick(int position) {

        Appointment appointment = ((AppointmentAdapter) appointments_add).GetAppointmentByPosition(position);

        Intent intent = new Intent(this, AppointmentInfo.class);
        intent.putExtra("barberNameKey", appointment.getBarber().getName());
        intent.putExtra("barberPhoneKey", appointment.getBarber().getPhoneNumber());
        intent.putExtra("barberAddressKey", appointment.getBarber().getAddress());
        intent.putExtra("barberDateKey", appointment.getDate() + " -- " + appointment.getTime());

        startActivity(intent);
        finish();
    }

    @Override
    public void onBarberClick(int position, int type)
    {
        Barber barber = null;
        if (type == 1)
        {
            barber = ((BarberAdapter) favourites_add).GetBarberByPosition(position);
        }
        else if (type == 2)
        {
            barber = ((BarberAdapter) populars_add).GetBarberByPosition(position);
        }

        Intent intent = new Intent(this, BarberInfo.class);
        assert barber != null; // check that "barber" is not null
        intent.putExtra("barberNameKey", barber.getName());
        intent.putExtra("barberPhoneKey", barber.getPhoneNumber());
        intent.putExtra("barberAddressKey", barber.getAddress());

        startActivity(intent);
        finish();
    }

    public void menuHome(View view) {

    }

    public void menuSearch(View view) {
        Intent intent = new Intent(this, SearchResultsActivity.class); // run the main class
        intent.putExtra("searchKey", "NULL"); // Example: sending a string value
        startActivity(intent);
        finish();
    }

    public void menuUser(View view) {
        Intent intent = new Intent(this, AccountSettings.class); // run the main class
        startActivity(intent);
        finish();
    }

    public void menuSettings(View view) {

    }
}