package com.example.yemek_takvimi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/*
    Bir kullanici giris yaptikten sonra gunluk menusune bakabilecegi activity'dir
    Database read(Okuma) yaparak islemini yapar.
 */

public class ana_menu extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_menu);

        // Initialize Firebase Authentication instance
        firebaseAuth = FirebaseAuth.getInstance();

        // Other initialization code...

        // Find the sign out button and set its onClickListener
        Button signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    // Method to sign out the user
    private void signOut() {
        firebaseAuth.signOut();

        // Redirect the user to the sign-in or sign-up activity
        startActivity(new Intent(this, sign_in.class));
        finish(); // Finish the current activity to prevent the user from coming back here using the back button
    }
}