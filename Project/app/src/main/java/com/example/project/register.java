package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void RegisterBt(View view)
    {

    }

    public void ToLogin(View view)
    {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
        finish();
    }
}