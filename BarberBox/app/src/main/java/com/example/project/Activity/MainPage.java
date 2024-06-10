package com.example.project.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainPage extends AppCompatActivity implements IRecyclerViewOnAppointmentClick, IRecyclerViewOnBarberClick {

    private RecyclerView appointments,favourites, populars;
    private RecyclerView.Adapter appointments_add, favourites_add, populars_add;
    EditText searchBar;
    TextView hiUsername, appointmentMsg, popularMsg, favouritesMsg;
    FirebaseAuth mAuth;
    FirebaseUser user;
    StorageReference storageReference;
    private FirebaseFirestore db;

    //Lock lock = new ReentrantLock();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // init view things
        appointments = findViewById(R.id.appointment_list);
        favourites = findViewById(R.id.favourites_list);
        populars = findViewById(R.id.popular_list);
        searchBar = findViewById(R.id.search_bar);
        hiUsername = findViewById(R.id.hi_username_text);
        appointmentMsg = findViewById(R.id.noAppointments);
        favouritesMsg = findViewById(R.id.noFavourrites);
        popularMsg = findViewById(R.id.noPopular);

        // init DB things
        mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser(); // FirebaseAuth.getInstance().getCurrentUser()
        db = FirebaseFirestore.getInstance();
        //lock.lock();

        if (user == null) // check if the user is connected
        {
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class); // run the main class
            startActivity(intent);
            finish();
        }

        hiUsername.setText("Hi, " + user.getDisplayName()); // change the name in the app to the username

        // init the RecyclerViews and the data from the Firestore and Storage
        FetchData();

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
        intent.putExtra("searchTerm", searchBar.getText().toString()); // Example: sending a string value
        startActivity(intent);
        finish();
    }

    private void FetchData()
    {
        if (Helper.barbers_ == null)
        {
            Helper.barbers_ = new ArrayList<Barber>();


            // Fetch data from Firestore
            db.collection("barbers").get()
                    .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {

                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Barber barber = document.toObject(Barber.class);
                            barber.set_id(document.getId());
                            Helper.barbers_.add(barber);

                            // get the appointments
                            db.collection("barbers")
                                    .document(document.getId())
                                    .collection("appointments")
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        QuerySnapshot appointmentsQuery = task1.getResult();
                                        List<QueryDocumentSnapshot> tempList = new ArrayList<>();
                                        appointmentsQuery.forEach(tempList::add);

                                        if (task1 != null)
                                        {
                                            for (QueryDocumentSnapshot appointment : appointmentsQuery)
                                            {
                                                try
                                                {
                                                    Appointment tempAppointment = appointment.toObject(Appointment.class);
                                                    tempAppointment.setDocumentName(appointment.getId());
                                                    tempAppointment.setBarber(barber);
                                                    barber.addAppointment(tempAppointment);

                                                    if (Objects.equals(tempAppointment.getUser_id(), user.getEmail()))
                                                    {
                                                        Helper.appointments_.add(tempAppointment);
                                                    }
                                                }
                                                finally
                                                {
                                                    if (appointment.equals(tempList.get(tempList.size() - 1))) // if it's the last barber
                                                    {
                                                        //lock.unlock();  // release the lock
                                                        initAppointment();
                                                    }
                                                }
                                            }
                                        }
                                    });

                            String picAdd = barber.getPicture_reference();
                            File file = Helper.getImageFile(barber.getName().replace(" ", "_") + ".png");

                            if (!file.exists()) // get the image only if the image in not exits in the device
                            {
                                storageReference = FirebaseStorage.getInstance().getReference(picAdd);
                                try {
                                    File localFile = File.createTempFile(barber.getName().replace(" ", "_"), ".png");

                                    storageReference.getFile(localFile)
                                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                                                {
                                                    Helper.SaveImage(localFile, barber.getName().replace(" ", "_") + ".png");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    // Handle any errors
                                                    exception.printStackTrace();
                                                }
                                            });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        initFavourites();
                        initPopular();
                    } else {
                        Log.d("MainActivity", "No barbers found");
                    }
                } else {
                    Log.d("MainActivity", "Error getting documents: ", task.getException());
                    // Handle error if necessary
                }
            });
        }
        else
        {
            initAppointment();
            initFavourites();
            initPopular();
        }


    }

    private void initAppointment()
    {
        // clear the appointment RecyclerView
        /*ArrayList<Appointment> tempAppointments = new ArrayList<>(Helper.appointments_);
        Helper.appointments_.clear();
        appointments_add.notifyDataSetChanged();

        Helper.appointments_ = new ArrayList<>(tempAppointments);*/

        // Initialize RecyclerView and set layout manager
        this.appointments = findViewById(R.id.appointment_list);
        this.appointments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        if (Helper.appointments_.isEmpty())
        {
            appointmentMsg.setVisibility(View.VISIBLE);
            return;
        }

        // Create and set adapter
        appointments_add = new AppointmentAdapter(Helper.appointments_, this);
        this.appointments.setAdapter(appointments_add);

        // Apply ItemSpacingDecoration to add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        this.appointments.addItemDecoration(new ItemSpacingDecorationRight(this, spacingInPixels));


    }

    private void initFavourites() {
        this.favourites = findViewById(R.id.favourites_list);
        this.favourites.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        if (Helper.barbers_.isEmpty()) // change to the real list
        {
            appointmentMsg.setVisibility(View.VISIBLE);
        }

        // Create and set adapter
        this.favourites_add = new BarberAdapter(Helper.barbers_, this, 1);
        this.favourites.setAdapter(favourites_add);

        // Apply ItemSpacingDecoration to add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        this.favourites.addItemDecoration(new ItemSpacingDecorationRight(this, spacingInPixels));
    }

    private void initPopular()
    {
        this.populars = findViewById(R.id.popular_list);
        this.populars.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        if (Helper.barbers_.isEmpty()) // change to the real list
        {
            appointmentMsg.setVisibility(View.VISIBLE);
        }
        // Create and set adapter
        this.populars_add = new BarberAdapter(Helper.barbers_, this, 1);
        this.populars.setAdapter(populars_add);

        // Apply ItemSpacingDecoration to add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        this.populars.addItemDecoration(new ItemSpacingDecorationRight(this, spacingInPixels));
    }


    @Override
    public void onAppointmentClick(int position) {

        Appointment appointment = ((AppointmentAdapter) appointments_add).GetAppointmentByPosition(position);

        Intent intent = new Intent(this, AppointmentInfo.class);
        intent.putExtra("barberId", appointment.getBarber().get_id());
        intent.putExtra("appointmentDate", appointment.getDate());

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
        intent.putExtra("barberId", barber.get_id());

        startActivity(intent);
        finish();
    }

    public void menuHome(View view) {

    }

    public void menuSearch(View view) {
        Intent intent = new Intent(this, SearchResultsActivity.class); // run the main class
        intent.putExtra("searchTerm", "NULL"); // Example: sending a string value
        startActivity(intent);
        finish();
    }

    public void menuUser(View view) {
        Intent intent = new Intent(this, AccountSettings.class); // run the main class
        startActivity(intent);
        finish();
    }
}