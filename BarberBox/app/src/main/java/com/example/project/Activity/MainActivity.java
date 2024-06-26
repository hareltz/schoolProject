package com.example.project.Activity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.Fragment.LoginFragment;
import com.example.project.R;
import com.example.project.Fragment.RegisterFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity
{
    FirebaseAuth auth;
    FirebaseUser user;
    public static final String CHANNEL_ID = "notifications";

    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
    private static final int READ_EXTERNAL_STORAGE_CODE = 2;
    private static final int NOTIFICATIONS_CODE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        FirebaseApp.initializeApp(this); // init firebase app


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
            }


            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
            }
        }

        if (!(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(this,
                    new String[]
                            {
                                    Manifest.permission.POST_NOTIFICATIONS
                            }, NOTIFICATIONS_CODE);
        }

        createNotificationChannel(); // create the notifications channel.

        if (user == null)
        {
            replaceFragment(new LoginFragment()); // if the user is not connected
        }
        else
        {
            toLoading(); // if the user is connected (going to the LoadingActivity)
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // using the super function

        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE)
        {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) // checks if permission not granted
            {
                Toast.makeText(this, "Storage write permission is denied, please allow write permission to get image",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == READ_EXTERNAL_STORAGE_CODE)
        {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) // checks if permission not granted
            {
                Toast.makeText(this, "Storage read permission is denied, please allow read permission to get image",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == NOTIFICATIONS_CODE)
        {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) // checks if permission not granted
            {
                Toast.makeText(this, "Notifications permission is denied, please allow permission.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    // this function switch between fragments.
    public void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    // this function is a listener that switch from RegisterFragment -> LoginFragment
    public void F_Login(View view)
    {
        replaceFragment(new LoginFragment());
    }

    // this function is a listener that switch from RegisterFragment -> LoginFragment
    public void F_Register(View view)
    {
        replaceFragment(new RegisterFragment());
    }

    // this function take the user to the Loading screen
    public void toLoading()
    {
        Intent intent = new Intent(this, Loading.class); // run the Loading class
        startActivity(intent);
        finish();
    }

    // this function creates the notification channel
    private void createNotificationChannel()
    {
        CharSequence name = "Barber Appointment Notification";
        String description = "This is a reminder that you have an appointment tomorrow. for more information enter the app.";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
