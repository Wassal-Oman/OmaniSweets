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
import com.jawaher.omanisweets.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomerHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        // initialize
        drawer = findViewById(R.id.drawer_layout);
        navigation = findViewById(R.id.nav_view);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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
    }

    @Override
    protected void onStart() {
        super.onStart();

        // get logged user
        FirebaseUser loggedUser = auth.getCurrentUser();

        if(loggedUser != null) {

            // get user details
            db.collection("users").document(loggedUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    // retrieve user data
                    User user = documentSnapshot.toObject(User.class);

                    // check if user exists
                    if(user != null) {
                        drawer_user_name.setText(user.getName());
                        drawer_user_email.setText(user.getEmail());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    auth.signOut();
                    finish();
                }
            });
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
            Toast.makeText(this, "ProfileActivity page will be available very soon!", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra("category", "Birthday");
        startActivity(intent);
    }

    public void selectCupcake(View view) {
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra("category", "Cupcake");
        startActivity(intent);
    }

    public void selectCandy(View view) {
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra("category", "Candy");
        startActivity(intent);
    }

    public void selectBlueberry(View view) {
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra("category", "Blueberry");
        startActivity(intent);
    }

    public void selectDonut(View view) {
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra("category", "Donut");
        startActivity(intent);
    }

    public void selectIcecream(View view) {
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra("category", "IceCream");
        startActivity(intent);
    }
}
