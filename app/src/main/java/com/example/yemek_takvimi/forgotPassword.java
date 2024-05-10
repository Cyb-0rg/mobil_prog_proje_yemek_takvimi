package com.example.yemek_takvimi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class forgotPassword extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText eEmailAddres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();

        eEmailAddres = findViewById(R.id.eEmailAddress_forgotPassword);


        /*Toolbar toolbar = findViewById(R.id.toolbar_goback_to_signin);
        setSupportActionBar(toolbar);

        // Enable the up button (back arrow)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
    }

    /*private void setSupportActionBar(Toolbar toolbar) {
        // Handle action bar item clicks
        if (toolbar.getId() == android.R.id.home) {
            // Respond to the action bar's Up/Home button
            onBackPressed(); // Navigate back

        }
        super.onOptionsItemSelected(toolbar);
    }*/


    public void resetPassword(View view) {
        String email = eEmailAddres.getText().toString().trim();;

        if (!TextUtils.isEmpty(email)) {
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Password reset email sent successfully
                                Toast.makeText(forgotPassword.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Failed to send password reset email
                                Toast.makeText(forgotPassword.this, "Failed to send password reset email. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // Empty email address
            Toast.makeText(forgotPassword.this, "Please enter your email address.", Toast.LENGTH_SHORT).show();
        }
    }

    public void redirectToSignUp(View view) {
        Intent intent = new Intent(this, sign_up.class);
        startActivity(intent);
        finish(); // Optionally, finish the SignInActivity to prevent going back to it using the back button

    }

    public void goback_signin(View view) {
        Intent intent = new Intent(this, sign_in.class);
        startActivity(intent);
        finish(); // Optionally, finish the SignInActivity to prevent going back to it using the back button

    }
}
