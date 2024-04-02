package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, MainPage.class); // run the main class
        startActivity(intent);
        finish();


    }

    public void openBarberProfile(View view)
    {
        EditText name = view.findViewById(R.id.barber_name);
        EditText phoneNumber = view.findViewById(R.id.barbers_phone);

        Intent intent = new Intent(this, BarberInfo.class);
        intent.putExtra("barberNameKey", name.getText().toString());
        intent.putExtra("barberPhoneKey", phoneNumber.getText().toString());
        intent.putExtra("barberAddressKey", "NULL"); // add this when I connect the DB (I just need to get the ID somehow / name)

        startActivity(intent);
        finish();
    }
}
