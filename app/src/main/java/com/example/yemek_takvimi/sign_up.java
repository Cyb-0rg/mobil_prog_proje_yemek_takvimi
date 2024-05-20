package com.example.yemek_takvimi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sign_up extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        emailEditText = findViewById(R.id.usernameEditText_reg);
        passwordEditText = findViewById(R.id.passwordEditText_reg);
        Button signUpButton = findViewById(R.id.signInButton_reg);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                createAccount(email, password);
            }
        });
    }

    private void createAccount(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(sign_up.this, "Sign up failed. Email address or password cannot be empty.",
                    Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {
                                    String userId = user.getUid();
                                    // Save user role to the database
                                    User newUser = new User(email, "user"); // "user" role
                                    databaseReference.child(userId).setValue(newUser)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(sign_up.this, "Sign up successful!",
                                                                Toast.LENGTH_SHORT).show();
                                                        // Navigate to the next activity or perform other actions
                                                    } else {
                                                        Toast.makeText(sign_up.this, "Failed to save user data. Please try again.",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            } else {
                                // Handle sign up failure with specific error messages
                                if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                    Toast.makeText(sign_up.this, "Sign up failed. Password needs to be at least 8 characters.",
                                            Toast.LENGTH_SHORT).show();
                                } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(sign_up.this, "Sign up failed. Invalid email address.",
                                            Toast.LENGTH_SHORT).show();
                                } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(sign_up.this, "Sign up failed. Email address already taken.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(sign_up.this, "Sign up failed. Please try again.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }
    }

    // User class to represent user data
    public static class User {
        public String email;
        public String role;

        public User(String email, String role) {
            this.email = email;
            this.role = role;
        }
    }


    public void redirectToSignIn(View view) {
        Intent intent = new Intent(this, sign_in.class);
        startActivity(intent);
    }



}



