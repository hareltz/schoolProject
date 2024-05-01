package com.example.project.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.R;

public class BarberInfo extends AppCompatActivity {

    TextView name, name2, phoneNum, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_barber_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // change this to get only the barber ID and get this info from the DB

        name = findViewById(R.id.barberInfo_name);
        name2 = findViewById(R.id.barberInfo_name2);
        phoneNum = findViewById(R.id.barberInfo_phone_number);
        address = findViewById(R.id.barberInfo_location);

        Intent intent = getIntent();
        name.setText(intent.getStringExtra("barberNameKey"));
        name2.setText(intent.getStringExtra("barberNameKey"));
        phoneNum.setText(intent.getStringExtra("barberPhoneKey"));
        address.setText(intent.getStringExtra("barberAddressKey"));

    }

    @SuppressLint("SetTextI18n")
    public void MakeAppointment(View view)
    {

        Intent intent = new Intent(this, AppointmentActivity.class); // run the main class
        intent.putExtra("nameKey", name.getText());
        intent.putExtra("picAddKey", "user_1");

        startActivity(intent);
        finish();
    }

    public void ArrowBack(View view)
    {
        Intent intent = new Intent(this, MainPage.class); // run the main class
        startActivity(intent);
        finish();
    }
}