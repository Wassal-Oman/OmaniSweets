package com.jawaher.omanisweets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jawaher.omanisweets.Models.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    // widgets
    ListView lvProducts;

    // data
    List<Product> products;
    MyAdapter adapter;
    String category;

    // firebase
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        // initialize
        lvProducts = findViewById(R.id.lv_products);
        products = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        // get passed data
        if(getIntent() != null) {
            category = getIntent().getStringExtra("category");
        }

        // load products
        loadProducts();
    }

    private void loadProducts() {

        // get products from Firebase Database
        db.collection("products").whereEqualTo("category", category).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {

                        Log.d("Product Data", document.getId() + " => " + document.getData());

                        // create a product object
                        Product product = document.toObject(Product.class);

                        // add product to product list
                        products.add(product);
                    }

                    // view products
                    if(products.size() > 0) {
                        viewProducts(products);
                    }
                } else {
                    Toast.makeText(ProductListActivity.this, "No Products Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void viewProducts(List<Product> products) {
        adapter = new MyAdapter(this, null, products);
        lvProducts.setDivider(null);
        lvProducts.setAdapter(adapter);
    }

    // private class to view data in ListView
    class MyAdapter extends BaseAdapter {

        private List<Product> data;
        private LayoutInflater inflater = null;
        Product product = null;

        public MyAdapter(Activity activity, Resources resources, List<Product> list) {
            this.data = list;
            inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if(data.size() <= 0){
                return 1;
            }

            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public class ViewHolder{

            public ImageView image;
            public TextView name;
            public TextView price;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            @SuppressLint("ViewHolder") View v = inflater.inflate(R.layout.custom_product_layout, null);
            MyAdapter.ViewHolder holder= new MyAdapter.ViewHolder();

            if(v != null){
                holder.image = v.findViewById(R.id.iv_product);
                holder.name = v.findViewById(R.id.tv_product_name);
                holder.price = v.findViewById(R.id.tv_product_price);
            }

            if(data.size() <= 0){
                holder.name.setText("No Name");
                holder.price.setText("No Price");
            } else {
                product = data.get(i);
                holder.name.setText(product.getName());
                holder.price.setText(product.getPrice());
                Picasso.get()
                        .load(product.getImage())
                        .resize(350, 300)
                        .centerCrop()
                        .into(holder.image);
            }

            return v;
        }
    }
}
