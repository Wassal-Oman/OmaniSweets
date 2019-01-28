package com.jawaher.omanisweets;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    // widgets
    EditText etEmail;

    // firebase
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        // initialize
        etEmail = findViewById(R.id.et_email);
        auth = FirebaseAuth.getInstance();
    }

    public void send(View view) {
        // get user input
        String email = etEmail.getText().toString().trim();

        if(email.isEmpty()) {
            Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // send a reset password email
        sendResetPasswordEmail(email);
    }

    private void sendResetPasswordEmail(String email) {
        final ProgressDialog dialog = ProgressDialog.show(this, "Sending...", "Please wait...", false);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                if(task.isSuccessful()) {
                    Toast.makeText(ForgetPasswordActivity.this, "Password reset link has been sent to you through your email", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void backToLogin(View view) {
        finish();
    }

    // method to validate email address
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
