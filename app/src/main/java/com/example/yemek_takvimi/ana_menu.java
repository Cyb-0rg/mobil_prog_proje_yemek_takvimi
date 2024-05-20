package com.example.yemek_takvimi;

import static com.example.yemek_takvimi.R.*;

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
        Button add_new_button = findViewById(R.id.addMenuButton);


        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        add_new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ana_menu.this, add_new_menu.class);
                startActivity(intent);
                //finish(); // Optional: finish the MainActivity so that the user can't go back to it using the back button

            }
        });

        // Initialize the view and edit button
        Button viewEditButton = findViewById(R.id.viewMenuButton);

        // Set OnClickListener for the view and edit button
        viewEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the edit_menu activity
                startActivity(new Intent(ana_menu.this, edit_menu.class));
            }
        });
    }

    // Method to sign out the user
    private void signOut() {
        firebaseAuth.signOut();

        // Redirect the user to the sign-in or sign-up activity
        startActivity(new Intent(ana_menu.this, sign_in.class));
        finish(); // Finish the current activity to prevent the user from coming back here using the back button
    }

}