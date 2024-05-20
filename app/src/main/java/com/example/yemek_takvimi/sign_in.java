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
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*

   Authentication icin yapilan activity'dir.
   Uygulama ilk acildigi zaman gorunen sayfadir.
   Authentication icin Firebox kullanir

 */


public class sign_in extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button signInButton = findViewById(R.id.signInButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                signIn(username, password);
            }
        });
    }

    private void signIn(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(sign_in.this, "Sign in failed. Email address or password cannot be empty.",
                    Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, get the signed-in user's information
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {
                                    String userId = user.getUid();
                                    // Retrieve the user's role from the database
                                    databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                String role = dataSnapshot.child("role").getValue(String.class);
                                                if ("admin".equals(role)) {
                                                    startActivity(new Intent(sign_in.this, ana_menu.class));
                                                } else if ("user".equals(role)) {
                                                    startActivity(new Intent(sign_in.this, landing_page.class));
                                                }
                                                finish(); // Finish the sign_in activity
                                            } else {
                                                Toast.makeText(sign_in.this, "Failed to retrieve user role. Please try again.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Toast.makeText(sign_in.this, "Database error: " + databaseError.getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(sign_in.this, "Authentication failed. Wrong credentials.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    public void redirectToSignUp(View view) {
        Intent intent = new Intent(this, sign_up.class);
        startActivity(intent);
        //finish(); // Optionally, finish the SignInActivity to prevent going back to it using the back button
    }

    public void redirectToForgotPassword(View view) {
        Intent intent = new Intent(this, forgotPassword.class);
        startActivity(intent);
        //finish(); // Optionally, finish the SignInActivity to prevent going back to it using the back button
    }

}
