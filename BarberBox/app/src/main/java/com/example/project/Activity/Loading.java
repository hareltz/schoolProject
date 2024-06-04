package com.example.project.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.Domain.Barber;
import com.example.project.Helper;
import com.example.project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Loading extends AppCompatActivity {

        private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
        private static final int READ_EXTERNAL_STORAGE_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loading);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if (ActivityCompat.checkSelfPermission(Loading.this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(Loading.this,
                                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
                    }

                    else
                    {
                        fetchImagesAndEnd();
                    }

                    if (ActivityCompat.checkSelfPermission(Loading.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(Loading.this,
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
                    }
                }

            }
        }, 4000);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) // checks if permission granted
            {
                fetchImagesAndEnd();
            }
            else
            {
                Toast.makeText(this, "Storage write permission is denied, please allow write permission to get image", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == READ_EXTERNAL_STORAGE_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) // checks if permission granted
            {

            }
            else
            {
                Toast.makeText(this, "Storage read permission is denied, please allow read permission to get image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // This function fetches images and then proceeds to the main screen
    private void fetchImagesAndEnd() {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // Fetch images asynchronously
            fetchImages();
        });

        // Wait for fetchImages() to complete, then execute end()
        future.join();
        end();
    }

    // This function take the user to the main screen
    public void end() {
        Intent intent = new Intent(this, MainPage.class); // run the main class
        startActivity(intent);
        finish();
    }

    // This function will fetch the images from Firebase
    private void fetchImages() {
        // Fetch data from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<Barber> barbers = new ArrayList<>();
        db.collection("barbers").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {

                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Barber barber = document.toObject(Barber.class);
                        barber.set_id(document.getId());
                        barbers.add(barber);

                        String picAdd = barber.getPicture_reference();
                        File file = Helper.getImageFile(barber.getName().replace(" ", "_") + ".png");

                        if (!file.exists()) // get the image only if the image in not exits in the device
                        {
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference(picAdd);
                            try {
                                File localFile = File.createTempFile(barber.getName().replace(" ", "_"), ".png");

                                storageReference.getFile(localFile)
                                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                                            {
                                                Helper.SaveImage(localFile, barber.getName().replace(" ", "_") + ".png");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Handle any errors
                                                exception.printStackTrace();
                                            }
                                        });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    Log.d("MainActivity", "No barbers found");
                }
            } else {
                Log.d("MainActivity", "Error getting documents: ", task.getException());
                // Handle error if necessary
            }
        });
    }
}