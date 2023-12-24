package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    EditText username, password;
    TextView msg;

    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.UsernameInput);
        password = findViewById(R.id.PasswordInput);
        msg = findViewById(R.id.msg);

        client = new Client();
        client.startConnection();
    }

    public void LoginBT(View view)
    {
        Log.println(Log.DEBUG,"debug", "username: " + username.getText() + " | password: " + password.getText());
        msg.setText("done!");
    }
}