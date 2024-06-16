package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.Helper;
import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class AccountSettings extends AppCompatActivity {

    private FirebaseUser user;

    EditText currentPassword, newPassword, newUsername, newEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        currentPassword = findViewById(R.id.AccountSettings_currentPassword);
        newPassword = findViewById(R.id.AccountSettings_newPassword);
        newUsername = findViewById(R.id.AccountSettings_newUsername);
        newEmail = findViewById(R.id.AccountSettings_newEmail);
    }

    public void ArrowBack(View view)
    {
        Intent intent = new Intent(this, MainPage.class); // go back to the main class
        startActivity(intent);
        finish();
    }

    public void UpdateAccountSettings(View view)
    {
        String current_password, new_password, new_email, new_username;

        current_password = currentPassword.getText().toString().trim();
        new_password = newPassword.getText().toString().trim();
        new_email = newEmail.getText().toString().trim();
        new_username = newUsername.getText().toString().trim();

        if (current_password.isEmpty())
        {
            Toast.makeText(AccountSettings.this, "Current password is a required field!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (!new_password.isEmpty())
            {
                updatePassword(new_password, current_password);
            }
            if (!new_email.isEmpty())
            {
                updateEmail(new_email, current_password);
            }
            if (!new_username.isEmpty())
            {
                updateUsername(new_username, current_password);
            }
        }
    }

    public void Logout(View view)
    {
        FirebaseAuth.getInstance().signOut(); // logout from firebase

        Intent intent = new Intent(this, MainActivity.class); // run the MainActivity class
        startActivity(intent);
        finish();
    }


    // this function checks if the old password is correct
    private void authenticate(String currentPassword, final OnAuthenticateListener listener)
    {
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
        user.reauthenticate(credential)
                .addOnCompleteListener(task ->
                {
                    if (task.isSuccessful())
                    {
                        listener.onSuccess();
                    } else {
                        Toast.makeText(AccountSettings.this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // this function update the email
    private void updateEmail(String newEmail, String currentPassword)
    {

        authenticate(currentPassword, () -> user.verifyBeforeUpdateEmail(newEmail)
                .addOnCompleteListener(task ->
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(AccountSettings.this, "Email updated.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AccountSettings.this, "Failed to update email.", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    // this function update the password
    private void updatePassword(String newPassword, String currentPassword) {

        if (!Objects.equals(Helper.checkPassword(newPassword), Helper.GOOD_PASSWORD))
        {
            Toast.makeText(AccountSettings.this, Helper.checkPassword(newPassword), Toast.LENGTH_SHORT).show();
        }
        else
        {
            authenticate(currentPassword, () -> user.updatePassword(newPassword)
                    .addOnCompleteListener(task ->
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(AccountSettings.this, "Password updated.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AccountSettings.this, "Failed to update password.", Toast.LENGTH_SHORT).show();
                        }
                    }));
        }
    }

    // this function update the username
    private void updateUsername(String newUsername, String currentPassword)
    {
        authenticate(currentPassword, () ->
        {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newUsername)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task ->
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(AccountSettings.this, "Username updated.", Toast.LENGTH_SHORT).show();
                        } else
                        {
                            Toast.makeText(AccountSettings.this, "Failed to update username.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    // an interface for this class only
    interface OnAuthenticateListener
    {
        void onSuccess();
    }
}