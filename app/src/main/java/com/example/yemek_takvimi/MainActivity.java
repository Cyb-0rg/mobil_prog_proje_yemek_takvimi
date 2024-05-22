package com.example.yemek_takvimi;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/*
    kullanici uyugulamayi actiginda Welcome Ekran olarak kullanilir.
    Büyük bir logo görüntüler
 */

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds delay for the splash screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if the user is already signed in
        if (isUserSignedIn())

            // If the user is not signed in, display the splash screen for a moment before redirecting to the sign-up page
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    redirectToSignUp();
                }
            }, SPLASH_DELAY);
        }

    // Check if the user is signed in (You can implement your logic here)
    private boolean isUserSignedIn() {
        // For simplicity, let's assume the user is not signed in initially
        return true;
    }

    public void redirectToForgotPassword(View view) {
        Intent intent = new Intent(this, forgotPassword.class);
        startActivity(intent);
        finish(); // Optionally, finish the SignInActivity to prevent going back to it using the back button

    }

    public void redirectToSignUp() {
        Intent intent = new Intent(MainActivity.this, sign_up.class);
        startActivity(intent);
        finish(); // Optional: finish the MainActivity so that the user can't go back to it using the back button

    }


}
