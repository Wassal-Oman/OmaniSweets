package com.jawaher.omanisweets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ProfileActivity extends AppCompatActivity {

    // widgets
    EditText etName, etEmail, etPhone;

    // attributes
    String name, email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // initialize
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);

        // get passed data
        if(getIntent() != null) {
            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            phone = getIntent().getStringExtra("phone");
        }

        // set widgets
        etName.setText(name);
        etEmail.setText(email);
        etPhone.setText(phone);
    }

    // back to home
    public void back(View view) {
        finish();
    }
}
