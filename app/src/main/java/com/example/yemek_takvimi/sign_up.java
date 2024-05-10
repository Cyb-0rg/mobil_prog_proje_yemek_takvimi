package com.example.yemek_takvimi;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

/*
    bu sayfada yeni kullancilar hesap acmak icin kayit olmalarina yardim eder
    Authentication icin Firebox kullanir
 */

public class sign_up extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

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

        if ( email.isEmpty() || password.isEmpty() ){
            Toast.makeText(sign_up.this, "Sign up failed. Email address or password can not be empty.",
                    Toast.LENGTH_SHORT).show();
        }else {


            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign up success, update UI with the signed-in user's information
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                // You can do additional actions here, like sending a verification email
                                // or navigating to another activity.
                                Toast.makeText(sign_up.this, "Sign up successful!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign up fails, display a more specific message to the user.
                                if (!task.isSuccessful() && task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                    Toast.makeText(sign_up.this, "Sign up failed. Password needs to be at least 8 characters.",
                                            Toast.LENGTH_SHORT).show();
                                } else if (!task.isSuccessful() && task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(sign_up.this, "Sign up failed. Invalid email address.",
                                            Toast.LENGTH_SHORT).show();
                                } else if (!task.isSuccessful() && task.getException() instanceof FirebaseAuthUserCollisionException) {
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

    public void redirectToSignIn(View view) {
        Intent intent = new Intent(this, sign_in.class);
        startActivity(intent);
    }



}



