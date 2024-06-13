package com.example.project.Activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class Loading extends AppCompatActivity {

        private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
        private static final int READ_EXTERNAL_STORAGE_CODE = 2;
        private static final int NOTIFICATIONS_CODE = 2;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ActivityCompat.checkSelfPermission(Loading.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(Loading.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
            }

            if (ActivityCompat.checkSelfPermission(Loading.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(Loading.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATIONS_CODE);
                }
            }
            setAlarm();
            fetchImages();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE)
        {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) // checks if permission not granted
            {
                Toast.makeText(this, "Storage write permission is denied, please allow write permission to get image", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == READ_EXTERNAL_STORAGE_CODE)
        {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) // checks if permission not granted
            {
                Toast.makeText(this, "Storage read permission is denied, please allow read permission to get image", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == NOTIFICATIONS_CODE)
        {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) // checks if permission not granted
            {
                Toast.makeText(this, "Notifications permission is denied, please allow permission.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // This function take the user to the main screen
    public void end() {
        Intent intent = new Intent(this, MainPage.class); // run the main class
        startActivity(intent);
        finish();
    }

    // This function will fetch the images from Firebase
    private void fetchImages()
    {
        // Fetch data from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Helper.barbers_ = new ArrayList<Barber>();
        Helper.appointments_ = new ArrayList<Appointment>();

        final int[] imagesCounter = {0};
        final int[] appointmentsCounter = {0};

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
                                Helper.barbers_.add(barber);

                                // get the appointments
                                db.collection("barbers")
                                        .document(document.getId())
                                        .collection("appointments")
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            QuerySnapshot appointmentsQuery = task1.getResult();
                                            if (task1 != null && appointmentsQuery != null && !appointmentsQuery.isEmpty())
                                            {
                                                for (QueryDocumentSnapshot appointment : appointmentsQuery)
                                                {
                                                    Appointment tempAppointment = appointment.toObject(Appointment.class);
                                                    tempAppointment.setDocumentName(appointment.getId());
                                                    tempAppointment.setBarber(barber);
                                                    barber.addAppointment(tempAppointment);

                                                    if (Objects.equals(tempAppointment.getUser_id(), FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                                                    {
                                                        Helper.appointments_.add(tempAppointment);
                                                    }

                                                    appointmentsCounter[0]++;
                                                    if (appointmentsCounter[0] ==  tempList.size() && imagesCounter[0] ==  tempList.size())
                                                    {
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
                                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                                                    {
                                                        Helper.SaveImage(localFile, barber.getName().replace(" ", "_") + ".png");
                                                        imagesCounter[0]++;
                                                        if (appointmentsCounter[0] ==  tempList.size() && imagesCounter[0] ==  tempList.size())
                                                        {
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
                                }
                                else
                                {
                                    imagesCounter[0]++;
                                    if (appointmentsCounter[0] ==  tempList.size() && imagesCounter[0] ==  tempList.size())
                                    {
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

    private void setAlarm() {
        // Set the time to trigger the notification (e.g., 5 seconds from now)
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 5);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}