package com.jawaher.omanisweets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jawaher.omanisweets.Models.User;

public class LoginActivity extends AppCompatActivity {

    // widgets
    private EditText etEmail;
    private EditText etPassword;

    // firebase authentication
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // check if user still logged in
        if(auth.getCurrentUser() != null) {
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    public void login(View view) {

        // get user inputs
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // validate user inputs
        if(email.isEmpty()) {
            Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // sign in user through firebase authentication
        signInUser(email, password);
    }

    // method to authenticate user through "Firebase Authentication Service" using email and password
    private void signInUser(String email, String password) {
        final ProgressDialog dialog = ProgressDialog.show(this, "Login In Progress...", "please wait...", false);
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dialog.dismiss();
                if(task.isSuccessful()) {
                    // get logged in user
                    FirebaseUser loggedUser = auth.getCurrentUser();
                    if(loggedUser != null) {
                        // check if email has been verified
                        if(loggedUser.isEmailVerified()) {
                            // get user data from firebase database
                            db.collection("users").document(loggedUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    // retrieve user data
                                    User user = documentSnapshot.toObject(User.class);

                                    // check if user exists
                                    if(user != null) {
                                        Toast.makeText(LoginActivity.this, "Welcome " + user.getName(), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Cannot retrieve user data", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Database Error", e.getMessage());
                                    Toast.makeText(LoginActivity.this, "Cannot retrieve user data", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "Email is not verified!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void goToForgetPassword(View view) {
        startActivity(new Intent(this, ForgetPasswordActivity.class));
    }

    // method to validate email address
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
