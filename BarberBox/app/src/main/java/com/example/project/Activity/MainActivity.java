package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.Fragment.LoginFragment;
import com.example.project.R;
import com.example.project.Fragment.RegisterFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null)
        {
            replaceFragment(new LoginFragment());
        }
        else
        {
            Intent intent = new Intent(this, MainPage.class); // run the main class
            startActivity(intent);
            finish();
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

    public void F_RegisterBT(View view)
    {
        // register code

        Intent intent = new Intent(this, MainPage.class); // run the main class
        startActivity(intent);
        finish();
    }


    public void F_LoginBT(View view)
    {
        // login code

        Intent intent = new Intent(this, MainPage.class); // run the main class
        startActivity(intent);
        finish();
    }
}
