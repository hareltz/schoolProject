package com.example.project.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.AlarmReceiver;
import com.example.project.Domain.Appointment;
import com.example.project.Domain.Barber;
import com.example.project.Helper;
import com.example.project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loading);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fetchData();
        // for testing
        Helper.setNotification(Loading.this, Timestamp.now());
    }

    // This function take the user to the main screen
    public void end() {
        Intent intent = new Intent(this, MainPage.class); // run the main class
        startActivity(intent);
        finish();
    }

    // This function will fetch the images from Firebase
    private void fetchData() {
        // Fetch data from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Helper.barbers = new ArrayList<Barber>();
        Helper.appointments = new ArrayList<Appointment>();

        final int[] imagesCounter = {0};
        final int[] appointmentsCounter = {0};

        Map<String, Object> dataToAdd = new HashMap<>();

        // load the favourites data
        db.collection("favourites")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        for (Map.Entry<String, Object> entry : document.getData().entrySet()) {
                            if ((boolean) entry.getValue()) {
                                Helper.favourites.put(entry.getKey(), (Boolean) entry.getValue());
                            }
                        }
                    }
                })
                .addOnFailureListener(task ->
                {
                    // create the favorites document
                    db.collection("favourites")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .set(dataToAdd)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure
                                    Log.w("TAG", "error adding " + FirebaseAuth.getInstance().getCurrentUser().getUid() + "to favourites.", e);
                                }
                            });
                });


        // Fetch data from Firestore
        db.collection("barbers").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            List<QueryDocumentSnapshot> tempList = new ArrayList<>();
                            querySnapshot.forEach(tempList::add);
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                Barber barber = document.toObject(Barber.class);
                                barber.set_id(document.getId());
                                Helper.barbers.add(barber);

                                // get the appointments
                                db.collection("barbers")
                                        .document(document.getId())
                                        .collection("appointments")
                                        .orderBy("time")
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            QuerySnapshot appointmentsQuery = task1.getResult();
                                            if (task1 != null && appointmentsQuery != null && !appointmentsQuery.isEmpty()) {
                                                for (QueryDocumentSnapshot appointment : appointmentsQuery) {
                                                    Appointment tempAppointment = appointment.toObject(Appointment.class);
                                                    tempAppointment.setDocumentName(appointment.getId());
                                                    tempAppointment.setBarber(barber);
                                                    barber.addAppointment(tempAppointment);

                                                    if (Objects.equals(tempAppointment.getUser_id(), FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                                        Helper.appointments.add(tempAppointment);
                                                    }

                                                    // check if we got all the appointments and images
                                                    appointmentsCounter[0]++;
                                                    if (appointmentsCounter[0] == tempList.size() && imagesCounter[0] == tempList.size()) {
                                                        end();
                                                    }
                                                }

                                            }
                                        });

                                String picAdd = barber.getPicture_reference();
                                File file = Helper.getImageFile(barber.getName().replace(" ", "_") + ".png");

                                if (!file.exists()) // get the image only if the image in not exits in the device
                                {
                                    StorageReference storageReference = FirebaseStorage.getInstance().getReference(picAdd);
                                    try {
                                        File localFile = File.createTempFile(barber.getName().replace(" ", "_"), ".png");

                                        storageReference.getFile(localFile)
                                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                        Helper.SaveImage(localFile, barber.getName().replace(" ", "_") + ".png");
                                                        imagesCounter[0]++;
                                                        if (appointmentsCounter[0] == tempList.size() && imagesCounter[0] == tempList.size()) {
                                                            end();
                                                        }
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
                                } else {
                                    // check if we got all the appointments and images
                                    imagesCounter[0]++;
                                    if (appointmentsCounter[0] == tempList.size() && imagesCounter[0] == tempList.size()) {
                                        end();
                                    }
                                }
                            }
                        } else {
                            Log.d("Loading", "No barbers found");
                        }
                    } else {
                        Log.d("Loading", "Error getting documents: ", task.getException());
                        // Handle error if necessary
                    }
                });
    }
}