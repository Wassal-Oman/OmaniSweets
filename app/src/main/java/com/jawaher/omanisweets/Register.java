package com.jawaher.omanisweets;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    // widgets
    private EditText etName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etPassword;

    // firebase authentication and database
    private FirebaseAuth auth;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initialize
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
    }

    public void register(View view) {
        // get user inputs
        final String name = etName.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // check user inputs
        if(!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !password.isEmpty()) {

            // create user through firebase authentication system
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // sign in succeed
                        FirebaseUser user = auth.getCurrentUser();

                        // check for created user
                        if(user != null) {
                            // create user hashmap
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("user_id", user.getUid().toString());
                            userMap.put("user_name", name);
                            userMap.put("user_email", email);
                            userMap.put("user_phone", "+968" + phone);
                            userMap.put("user_type", "customer");

                            // add user to database
                            database.collection("users").document(user.getUid()).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Register.this, "New user has been registered successfully!", Toast.LENGTH_SHORT).show();

                                    // sign out
                                    auth.signOut();

                                    // go back to login
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register.this, "ERROR, cannot register user in database due to " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(Register.this, "Cannot create user!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // sign in fails
                        Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
        }
    }

    public void backToLogin(View view) {
        finish();
    }
}
