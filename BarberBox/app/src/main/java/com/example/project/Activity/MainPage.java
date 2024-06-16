package com.example.project.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.project.Helper;
import com.example.project.Interfaces.IRecyclerViewOnAppointmentClick;
import com.example.project.Interfaces.IRecyclerViewOnBarberClick;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainPage extends AppCompatActivity implements IRecyclerViewOnAppointmentClick, IRecyclerViewOnBarberClick {

    private RecyclerView appointments,favourites, allBarbers;
    private RecyclerView.Adapter appointments_add, favourites_add, allBarbers_add;
    EditText searchBar;
    TextView hiUsername, appointmentMsg, favouritesMsg;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // init view things
        appointments = findViewById(R.id.appointment_list);
        favourites = findViewById(R.id.favourites_list);
        allBarbers = findViewById(R.id.all_list);
        searchBar = findViewById(R.id.search_bar);
        hiUsername = findViewById(R.id.hi_username_text);
        appointmentMsg = findViewById(R.id.noAppointments);
        favouritesMsg = findViewById(R.id.noFavourrites);

        // init DB things
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user == null) // check if the user is connected
        {
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class); // run the mainActivity
            startActivity(intent);
            finish();
        }

        hiUsername.setText("Hi, " + user.getDisplayName()); // change the name in the app to the username

        // init the RecyclerViews and the data from the Firestore and Storage;
        initAppointment();
        initFavourites();
        initAllBarbers();

        // Set the OnEditorActionListener inside onCreate method
        // function that detects when the user press enter on the search_bar EditText
        searchBar.setOnEditorActionListener((v, actionId, event) ->
        {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null &&
                    event.getAction() == KeyEvent.ACTION_DOWN &&
                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
            {
                EnterSearchResults();
                return true;
            }
            return false;
        });
    }

    private void EnterSearchResults()
    {
        Intent intent = new Intent(this, SearchResultsActivity.class); // run the main class
        intent.putExtra("searchTerm", searchBar.getText().toString()); // Example: sending a string value
        startActivity(intent);
        finish();
    }

    private void initAppointment()
    {
        // Initialize RecyclerView and set layout manager
        this.appointments = findViewById(R.id.appointment_list);
        this.appointments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        if (Helper.appointments == null)
        {
            return;
        }
        else if (Helper.appointments.isEmpty())
        {
            appointmentMsg.setVisibility(View.VISIBLE);
            return;
        }

        // Create and set adapter
        appointments_add = new AppointmentAdapter(Helper.appointments, this);
        this.appointments.setAdapter(appointments_add);

        // Apply ItemSpacingDecoration to add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        this.appointments.addItemDecoration(new ItemSpacingDecorationRight(this, spacingInPixels));


    }

    private void initFavourites()
    {

        this.favourites = findViewById(R.id.favourites_list);
        this.favourites.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<Barber> barbers = new ArrayList<>();

        for (Barber barber : Helper.barbers)
        {
            if (Helper.favourites.containsKey(barber.get_id()))
            {
                if (Boolean.TRUE.equals(Helper.favourites.get(barber.get_id())))
                {
                    barbers.add(barber);
                }
            }
        }

        if (barbers.isEmpty())
        {
            favouritesMsg.setVisibility(View.VISIBLE);
            return;
        }

        // Create and set adapter
        this.favourites_add = new BarberAdapter(barbers, this, 1);
        this.favourites.setAdapter(favourites_add);

        // Apply ItemSpacingDecoration to add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        this.favourites.addItemDecoration(new ItemSpacingDecorationRight(this, spacingInPixels));
    }

    private void initAllBarbers()
    {
        this.allBarbers = findViewById(R.id.all_list);
        this.allBarbers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        if (Helper.barbers == null)
        {
            return;
        }
        else if (Helper.barbers.isEmpty()) // change to the real list
        {
            appointmentMsg.setVisibility(View.VISIBLE);
        }

        // Create and set adapter
        this.allBarbers_add = new BarberAdapter(Helper.barbers, this, 2);
        this.allBarbers.setAdapter(allBarbers_add);

        // Apply ItemSpacingDecoration to add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        this.allBarbers.addItemDecoration(new ItemSpacingDecorationRight(this, spacingInPixels));
    }


    @Override
    public void onAppointmentClick(int position)
    {

        Appointment appointment = ((AppointmentAdapter) appointments_add).GetAppointmentByPosition(position);

        Intent intent = new Intent(this, AppointmentInfo.class);
        intent.putExtra("barberId", appointment.getBarber().get_id());
        intent.putExtra("appointmentDocumentName", appointment.getDocumentName());

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
            barber = ((BarberAdapter) allBarbers_add).GetBarberByPosition(position);
        }

        Intent intent = new Intent(this, BarberInfo.class);
        assert barber != null; // check that "barber" is not null
        intent.putExtra("barberId", barber.get_id());

        startActivity(intent);
        finish();
    }

    public void menuHome(View view)
    {
        // we already here
    }

    public void menuSearch(View view)
    {
        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra("searchTerm", "");
        startActivity(intent);
        finish();
    }

    public void menuUser(View view)
    {
        Intent intent = new Intent(this, AccountSettings.class); // run the main class
        startActivity(intent);
        finish();
    }
}