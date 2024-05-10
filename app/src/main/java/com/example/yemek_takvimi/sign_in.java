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

/*

   Authentication icin yapilan activity'dir.
   Uygulama ilk acildigi zaman gorunen sayfadir.
   Authentication icin Firebox kullanir

 */


public class sign_in extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();

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

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(sign_in.this, "Sign in failed. Email address or password can not be empty.",
                    Toast.LENGTH_SHORT).show();
        }else {


            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                //  navigate to the main menu page here
                                startActivity(new Intent(sign_in.this, ana_menu.class));
                                finish(); // Finish the SignInActivity so the user can't go back to it
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(sign_in.this, "Authentication failed. Wrong credentials",
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
