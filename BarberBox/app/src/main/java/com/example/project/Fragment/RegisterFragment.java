package com.example.project.Fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project.Activity.MainActivity;
import com.example.project.Helper;
import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class RegisterFragment extends Fragment
{

    TextView registerBT;
    EditText emailField, passwordField;
    FirebaseAuth mAuth;

    private MainActivity mainActivity;

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        // Cast the context (which is the activity) to the specific activity class
        if (context instanceof MainActivity)
        {
            mainActivity = (MainActivity) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must be an instance of MainActivity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mAuth = FirebaseAuth.getInstance();
        registerBT = view.findViewById(R.id.f_registerBt);
        emailField = view.findViewById(R.id.f_enterEmailRegister);
        passwordField = view.findViewById(R.id.f_enterPasswordRegister);


        registerBT.setOnClickListener(v ->
        {
            if (passwordField.toString().isEmpty() || emailField.toString().isEmpty())
            {
                Toast.makeText(getActivity(), "you have to enter things",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                String password = String.valueOf(passwordField.getText());
                String email = String.valueOf(emailField.getText());
                String username = Helper.getUsername(email);
                String output = Helper.checkPassword(password);

                if (Objects.equals(output, Helper.GOOD_PASSWORD))
                {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task ->
                            {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(getActivity(),
                                            "Authentication succeed.",
                                            Toast.LENGTH_SHORT).show();

                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if (user != null)
                                    {
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(username)
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(profileUpdateTask -> { });
                                    }

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                    {
                                        mainActivity.toLoading();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),
                                            "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                {
                    Toast.makeText(getActivity(), output,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}