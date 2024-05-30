package com.example.project.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment
{

    TextView registerBT;
    EditText emailField, passwordField;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        registerBT = view.findViewById(R.id.f_loginBt);
        emailField = view.findViewById(R.id.f_enterEmailLogin);
        passwordField = view.findViewById(R.id.f_enterPasswordLogin);

        registerBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordField.getText().toString().trim().isEmpty() || emailField.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getActivity(), "The password or username is missing.",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String password = String.valueOf(passwordField.getText());
                    String email = String.valueOf(emailField.getText());

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(getActivity(), "Authentication succeed.",
                                                Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(getActivity(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
        });


        return view;
    }

    public void LoginBT(View view)
    {

    }

    public void ToRegister(View view)
    {
        /*if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(new RegisterFragment());
        }*/
    }
}