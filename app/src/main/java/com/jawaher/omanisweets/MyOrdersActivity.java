package com.jawaher.omanisweets;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jawaher.omanisweets.Models.Order;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {

    // widgets
    ListView lvOrders;

    // attributes
    List<Order> orders;
    ArrayAdapter adapter;

    // firebase
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        // initialize
        lvOrders = findViewById(R.id.lv_orders);
        orders = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // check for default action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // load orders
        final ProgressDialog dialog = ProgressDialog.show(this, "Loading Order List", "Please wait...", false, false);
        db.collection("orders").whereEqualTo("customer_id", auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                dialog.dismiss();
                for(QueryDocumentSnapshot document : task.getResult()) {
                    Order order = document.toObject(Order.class);
                    orders.add(order);
                }

                // view order list
                viewOrderList(orders);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void viewOrderList(List<Order> orders) {
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, orders);
        lvOrders.setAdapter(adapter);
    }
}
