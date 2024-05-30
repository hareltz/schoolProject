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

import com.example.project.Helper;
import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterFragment extends Fragment {

    TextView registerBT;
    EditText emailField, passwordField;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mAuth = FirebaseAuth.getInstance();
        registerBT = view.findViewById(R.id.f_registerBt);
        emailField = view.findViewById(R.id.f_enterEmailRegister);
        passwordField = view.findViewById(R.id.f_enterPasswordRegister);


        registerBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (passwordField.toString().isEmpty() || emailField.toString().isEmpty())
                {
                    Toast.makeText(getActivity(), "you have to enter things",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    String password = String.valueOf(passwordField.getText());
                    String email = String.valueOf(emailField.getText());
                    String username = Helper.getUsername(email);
                    String output = Helper.checkPassword(password);

                    if (output == Helper.GOOD_PASSWORD)
                    {
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Authentication succeed.",
                                                    Toast.LENGTH_SHORT).show();

                                            FirebaseUser user = mAuth.getCurrentUser();

                                            if (user != null) {
                                                // Update the user profile with the desired username
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(username) // Replace 'username' with the actual username variable
                                                        .build();

                                                user.updateProfile(profileUpdates)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> profileUpdateTask) {
                                                            }
                                                        });
                                            }

                                            FirebaseAuth.getInstance().signOut();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(getActivity(), "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else
                    {
                        Toast.makeText(getActivity(), output,
                                Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

        return view;
    }

    public void RegisterBt(View view) {
    }

    public void F_Login(View view) {
    }

}