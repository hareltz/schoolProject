package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;

public class AccountSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void ArrowBack(View view)
    {
        Intent intent = new Intent(this, MainPage.class); // go back to the main class
        startActivity(intent);
        finish();
    }

    public void UpdateAccountSettings(View view)
    {
        // firebase code here
    }

    public void Logout(View view)
    {
        FirebaseAuth.getInstance().signOut(); // logout from firebase

        Intent intent = new Intent(this, MainActivity.class); // run the MainActivity class
        startActivity(intent);
        finish();
    }
}