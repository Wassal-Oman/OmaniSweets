package com.jawaher.omanisweets;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    // widgets
    private EditText etEmail;
    private EditText etPassword;

    // firebase authentication
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // get logged user
        FirebaseUser user = auth.getCurrentUser();

        if(user != null) {
            startActivity(new Intent(this, Home.class));
        }
    }

    public void login(View view) {

        // get user inputs
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // check user input
        if(!email.isEmpty() && !password.isEmpty()) {

            // make sign in through firebase authentication system
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // sign in successful
                        Toast.makeText(Login.this, "Welcome", Toast.LENGTH_SHORT).show();

                        // go to home page
                        startActivity(new Intent(Login.this, Home.class));
                    } else {
                        // sign in failed
                        Toast.makeText(Login.this, "Wrong email or password!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
        }
    }

    public void register(View view) {
        startActivity(new Intent(this, Register.class));
    }
}
