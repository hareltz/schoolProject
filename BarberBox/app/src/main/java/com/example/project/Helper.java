package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Environment;

import com.example.project.Domain.Appointment;
import com.example.project.Domain.Barber;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Helper {

    public static final String GOOD_PASSWORD = "GOOD PASSWORD"; // Constant for a good password
    public static ArrayList<Barber> barbers_; // arrayList for the barbers
    public static ArrayList<Appointment> appointments_ = new ArrayList<>(); // arrayList for the appointments

    /*public static void openLocationInGoogleMaps(double latitude, double longitude) {
        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }*/

    // this function check if the password is
    // 1. Password is at least 8 characters long.
    // 2. Password contains at least one uppercase letter.
    // 3. Password contains at least one lowercase letter.
    // 4. Password contains at least one digit.
    public static String checkPassword(String password)
    {

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
    public static String getDateFromTimestamp(Timestamp timestamp) {
        Date date = timestamp.toDate(); // convert the timestamp to Date

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(date); // get the string as a date

        return formattedDate;
    }

    // Sample method to get the time from GeoPoint
    public static String getTimeFromTimestamp(Timestamp timestamp) {
        Date date = timestamp.toDate(); // convert the timestamp to Date

        SimpleDateFormat dateFormat = new SimpleDateFormat(" HH:mm", Locale.getDefault());
        String formattedDate = dateFormat.format(date); // get the string time

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

    // this function return the barber with the barberId
    public static Barber getBarberDataById(String barberId) {
        for (Barber barber : barbers_)
        {
            if (Objects.equals(barber.get_id(), barberId))
            {
                return barber;
            }
        }
        return null;
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
        File dir = new File(path+"/Android/data/barber_box");
        dir.mkdirs();

        File file = new File(dir, imageName);

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
        File dir = new File(path + "/Android/data/barber_box");
        File localFile = new File(dir, imageName);
        return localFile;
    }

    public static String getAddressFromGeoPoint(GeoPoint geoPoint, Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder addressString = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressString.append(address.getAddressLine(i)).append("\n");
                }
                return addressString.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Comparator to compare Appointments by their time
    public static class AppointmentTimeComparator implements Comparator<Appointment> {
        @Override
        public int compare(Appointment a1, Appointment a2) {
            return a1.getTime().compareTo(a2.getTime());
        }
    }
}
