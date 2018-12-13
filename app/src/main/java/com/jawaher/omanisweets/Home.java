package com.jawaher.omanisweets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // user name and email
    String name = "", email = "";

    // widgets
    TextView drawer_user_name;
    TextView drawer_user_email;

    // drawer
    DrawerLayout drawer;
    NavigationView navigation;

    // firebase authentication and database
    private FirebaseAuth auth;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // initialize
        drawer = findViewById(R.id.drawer_layout);
        navigation = findViewById(R.id.nav_view);
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        // show home button with title
        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // initialize header widgets
        View header = navigation.getHeaderView(0);
        drawer_user_name = header.findViewById(R.id.drawer_user_name);
        drawer_user_email= header.findViewById(R.id.drawer_user_email);

        // set navigation listener
        navigation.setNavigationItemSelectedListener(this);

        // load user data
        loadUserData(name, email);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // get logged user
        FirebaseUser user = auth.getCurrentUser();

        if(user != null) {

            // get user details
            database.collection("users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    drawer_user_name.setText(documentSnapshot.get("user_name").toString());
                    drawer_user_email.setText(documentSnapshot.get("user_email").toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Home.this, "Cannot find looged user due to " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadUserData(String name, String email) {
        // check for shared preferences
        SharedPreferences sp = getSharedPreferences("USER", MODE_PRIVATE);
        String data = sp.getString("user", "");

        // check of there is any available data
        if(!data.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(data);

                // get user name
                name = jsonObject.getString("user_name");
                email = jsonObject.getString("user_email");

                // set drawer views
                drawer_user_name.setText(name);
                drawer_user_email.setText(email);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == android.R.id.home) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Toast.makeText(this, "Profile page will be available very soon!", Toast.LENGTH_SHORT).show();
        } else if(id == R.id.nav_orders) {
            Toast.makeText(this, "Orders page will be available very soon!", Toast.LENGTH_SHORT).show();
        } else if(id == R.id.nav_exit) {
            // sign out
            auth.signOut();
            finish();
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void selectBirthday(View view) {
        Toast.makeText(this, "Birthday", Toast.LENGTH_SHORT).show();
    }

    public void selectCupcake(View view) {
        Toast.makeText(this, "Cupcake", Toast.LENGTH_SHORT).show();
    }

    public void selectCandy(View view) {
        Toast.makeText(this, "Candy", Toast.LENGTH_SHORT).show();
    }

    public void selectBlueberry(View view) {
        Toast.makeText(this, "Blueberry", Toast.LENGTH_SHORT).show();
    }

    public void selectDonut(View view) {
        Toast.makeText(this, "Donut", Toast.LENGTH_SHORT).show();
    }

    public void selectIcecream(View view) {
        Toast.makeText(this, "Ice Cream", Toast.LENGTH_SHORT).show();
    }
}
