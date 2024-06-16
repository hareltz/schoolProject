package com.example.project;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.os.Environment;
import android.provider.CalendarContract;

import com.example.project.BroadcastReceiver.AlarmReceiver;
import com.example.project.Domain.Appointment;
import com.example.project.Domain.Barber;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Helper {

    public static final String GOOD_PASSWORD = "GOOD PASSWORD"; // Constant for a good password
    public static ArrayList<Barber> barbers; // arrayList for the barbers
    public static Map<String, Boolean> favourites = new HashMap<>();
    public static ArrayList<Appointment> appointments = new ArrayList<>(); // arrayList for the appointments

    // this function creates a new notification at timestamp
    public static void setNotification(Context context, Timestamp timestamp) {
        // Convert Firebase Timestamp to java.util.Date
        Date date = timestamp.toDate();

        // Create a Calendar instance and set it to the date from Firebase Timestamp
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Subtract one day for the notification time
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        // Create an intent and pending intent for the notification
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE );

        // Get the AlarmManager service and set the exact alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    // this function add an event to the calendar at timestamp
    public static void addEventToCalendar(Context context, Timestamp timestamp, Barber barber)
    {
        // Convert Firebase Timestamp to Date
        Date date = timestamp.toDate();

        // Use Calendar to set the event time
        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(date);

        // Assuming the event is 1 hour long
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(date);
        endTime.add(Calendar.HOUR, 1);

        // Create an intent and put extra data for the event
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, "Barber Appointment at " + barber.getName())
                .putExtra(CalendarContract.Events.DESCRIPTION, "This is a barber appointment at " + barber.getName())
                .putExtra(CalendarContract.Events.EVENT_LOCATION, Helper.getAddressFromGeoPoint(barber.getLocation(), context))
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        // Start the activity with the intent
        context.startActivity(intent);
    }

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
    public static String getDateFromTimestamp(Timestamp timestamp)
    {
        Date date = timestamp.toDate(); // convert the timestamp to Date

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(date); // get the string as a date

        return formattedDate;
    }

    // Sample method to get the time from GeoPoint
    public static String getTimeFromTimestamp(Timestamp timestamp)
    {
        Date date = timestamp.toDate(); // convert the timestamp to Date

        SimpleDateFormat dateFormat = new SimpleDateFormat(" HH:mm", Locale.getDefault());
        String formattedDate = dateFormat.format(date); // get the string time

        return formattedDate;
    }

    // this function get the username@gmail.com from the email ("username" in our case)
    public static String getUsername(String email)
    {
        int atIndex = email.indexOf('@');
        if (atIndex != -1)
        {
            return email.substring(0, atIndex);
        }
        else
        {
            return "user";
        }
    }

    // this function return the barber with the barberId
    public static Barber getBarberDataById(String barberId)
    {
        for (Barber barber : barbers)
        {
            if (Objects.equals(barber.get_id(), barberId))
            {
                return barber;
            }
        }
        return null;
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

    public static String getAddressFromGeoPoint(GeoPoint geoPoint, Context context)
    {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try
        {
            List<Address> addresses = geocoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty())
            {
                Address address = addresses.get(0);
                StringBuilder addressString = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++)
                {
                    addressString.append(address.getAddressLine(i)).append("\n");
                }
                return addressString.toString();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    // Comparator to compare Appointments by their time
    public static class AppointmentTimeComparator implements Comparator<Appointment>
    {
        @Override
        public int compare(Appointment a1, Appointment a2)
        {
            return a1.getTime().compareTo(a2.getTime());
        }
    }
}
