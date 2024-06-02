package com.example.project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project.Domain.Barber;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class Helper {

    // Constant for a good password
    public static final String GOOD_PASSWORD = "GOOD PASSWORD";

    public static String checkPassword(String password) {

        // Check if the password is at least 8 characters long
        if (password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }

        // Check if the password contains at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter.";
        }

        // Check if the password contains at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter.";
        }

        // Check if the password contains at least one digit
        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one digit.\n";
        }

        return GOOD_PASSWORD;

    }

    // Sample method to get date from GeoPoint
    public static String getDateFromGeoPoint(Timestamp timestamp) {
        // Step 1: Convert the timestamp to Date
        Date date = timestamp.toDate();

        // Step 2: Format the Date to a string
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(date);

        return formattedDate;
    }

    // Sample method to get the time from GeoPoint
    public static String getTimeFromGeoPoint(Timestamp timestamp) {
        // Step 1: Convert the timestamp to Date
        Date date = timestamp.toDate();

        // Step 2: Format the Time to a string
        SimpleDateFormat dateFormat = new SimpleDateFormat(" HH:mm", Locale.getDefault());
        String formattedDate = dateFormat.format(date);

        return formattedDate;
    }

    // this function get the username@gmail.com from the email ("username" in our case)
    public static String getUsername(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex != -1) {
            return email.substring(0, atIndex);
        } else {
            return "user";
        }
    }

    // this function will return a list with all the barbers in firebase
    public static void getBarbersData(FirebaseFirestore db, BarbersDataListener listener) {
        ArrayList<Barber> barbers = new ArrayList<>();
        /*CollectionReference barbersRef = db.collection("barbers");
        barbersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Barber barber = document.toObject(Barber.class);
                        barber.set_id(document.getId());
                        barbers.add(barber);
                    }
                    listener.onDataLoaded(barbers);
                } else {
                    listener.onError("Query snapshot is null");
                }
            } else {
                listener.onError(task.getException().getMessage());
            }
        });*/

        db.collection("barbers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                for (QueryDocumentSnapshot document : querySnapshot) {
                                    Barber barber = document.toObject(Barber.class);
                                    barber.set_id(document.getId());
                                    barbers.add(barber);
                                }
                                listener.onDataLoaded(barbers);
                            } else {
                                listener.onError("Query snapshot is null");
                            }
                        } else {
                            listener.onError(task.getException().getMessage());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MYERROR", "Error getting documents.", e);
                    }
                });
    }

    // this function return the barber with the barberId
    public static Barber getBarberDataById(FirebaseFirestore db, String barberId) {
        // Reference to the specific barber document
        DocumentReference barberRef = db.collection("barbers").document(barberId);

        final Barber[] barber = new Barber[1];

        barberRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Convert the document to a Barber object
                    Barber tempBarber = document.toObject(Barber.class);
                    if (tempBarber != null) {
                        // Do something with the barber object
                        Log.d("Barber", "Name: " + tempBarber.getName());
                        // Here you can access other properties of the barber object
                        barber[0] = tempBarber;
                    }
                } else {
                    Log.d("MainActivity", "No such a barber with this id");
                }
            } else {
                Log.w("MainActivity", "Error getting barber", task.getException());
            }
        });

        return barber[0];
    }

    //The BarbersDataListener helps us handle when the data for barbers is loaded or if there's an error during the process.
    public interface BarbersDataListener {
        void onDataLoaded(ArrayList<Barber> barbers);
        void onError(String errorMessage);
    }

    public static void SaveImage(File localFile, String imageName)
    {
        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path+"/DCIM/BARBER_BOX");
        dir.mkdirs();
        String picName = imageName;
        File file = new File(dir, picName);

        OutputStream out;

        try
        {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static File getImageFile(String imageName)
    {
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path + "/DCIM/BARBER_BOX");
        File localFile = new File(dir, imageName);
        return localFile;
    }
}
