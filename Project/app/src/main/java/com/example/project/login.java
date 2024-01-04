package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class login extends AppCompatActivity {

    private EditText username, password;
    private TextView msg;
    // private Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.UsernameInput);
        password = findViewById(R.id.PasswordInput);
        msg = findViewById(R.id.msg);

        try{
            // client = new Client();

        } catch (RuntimeException e)
        {
            Log.println(Log.DEBUG,"debug", e.getMessage());
        }

    }

    public void LoginBT(View view)
    {
        Log.println(Log.DEBUG,"debug", "username: " + username.getText() + " | password: " + password.getText());

        Intent intent = new Intent(this, register.class); // run the main class
        startActivity(intent);
        finish();
    }

    public void ToRegister(View view)
    {
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
        finish();
    }
}