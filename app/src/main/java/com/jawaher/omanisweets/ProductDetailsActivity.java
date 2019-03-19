package com.jawaher.omanisweets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {

    // widgets
    ImageView ivImage;
    EditText etPrice, etCategory;

    // attributes
    String name, price, image, category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // initialize
        etPrice = findViewById(R.id.et_price);
        etCategory = findViewById(R.id.et_category);
        ivImage = findViewById(R.id.iv_image);

        // check for passed data
        if(getIntent() != null) {
            name = getIntent().getStringExtra("name");
            price = getIntent().getStringExtra("price");
            image = getIntent().getStringExtra("image");
            category = getIntent().getStringExtra("category");
        }

        // check for default action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(name);
        }

        // set widgets
        etPrice.setText("Price: "+ price + " OMR");
        etCategory.setText("Category: " + category);
        Picasso.get()
                .load(image)
                .resize(300, 300)
                .centerCrop()
                .into(ivImage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // method to trigger make order button
    public void orderProduct(View view) {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("product_name", name);
        intent.putExtra("product_price", price);
        intent.putExtra("product_image", image);
        intent.putExtra("product_category", category);
        startActivity(intent);
    }
}
