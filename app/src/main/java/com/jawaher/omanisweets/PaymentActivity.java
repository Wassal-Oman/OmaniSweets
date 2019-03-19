package com.jawaher.omanisweets;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    // widgets
    EditText etCardNumber, etMonth, etYear, etCVC, etDate, etTime, etCount;

    // attributes
    Calendar calendar;
    String customerName, customerPhone;
    String product_name, product_price, product_category, product_image;
    int count = 1;

    // firebase
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // initialize
        etCardNumber = findViewById(R.id.et_card_number);
        etMonth = findViewById(R.id.et_month);
        etYear = findViewById(R.id.et_year);
        etCVC = findViewById(R.id.et_cvc);
        etDate = findViewById(R.id.et_date);
        etTime = findViewById(R.id.et_time);
        etCount = findViewById(R.id.et_count);
        calendar = Calendar.getInstance();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // get default action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // get passed values
        if(getIntent() != null) {
            product_name = getIntent().getStringExtra("product_name");
            product_price = getIntent().getStringExtra("product_price");
            product_category = getIntent().getStringExtra("product_category");
            product_image = getIntent().getStringExtra("product_image");
        }

        // set count
        etCount.setText(String.valueOf(count));
    }

    @Override
    protected void onStart() {
        super.onStart();

        // get user data
        SharedPreferences sp = getSharedPreferences("USER", MODE_PRIVATE);
        customerName = sp.getString("name", "");
        customerPhone = sp.getString("phone", "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // method to trigger date button click
    public void pickDate(View view) {
        // date picker
        DatePickerDialog.OnDateSetListener mDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // get date
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                etDate.setText(sdf.format(calendar.getTime()));
            }
        };

        new DatePickerDialog(this, mDatePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    // method to trigger time button click
    public void pickTime(View view) {

        // get hours and minutes
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // time picker
        TimePickerDialog mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                etTime.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);

        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    // method to trigger minus button click
    public void countDown(View view) {
        if(count > 1) {
            count -= 1;
        } else {
            count = 1;
        }

        etCount.setText(String.valueOf(count));
    }

    // method to trigger plus button up
    public void countUp(View view) {
        count += 1;
        etCount.setText(String.valueOf(count));
    }

    public void orderNow(View view) {
        // get user inputs
        String cardNumber = etCardNumber.getText().toString().trim();
        String month = etMonth.getText().toString().trim();
        String year = etYear.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String cvc = etCVC.getText().toString().trim();

        // validation
        if(customerName.isEmpty() || customerPhone.isEmpty()) {
            Toast.makeText(this, "You are not logged in", Toast.LENGTH_SHORT).show();
            auth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        if(cardNumber.isEmpty()) {
            Toast.makeText(this, "Please enter card number", Toast.LENGTH_SHORT).show();
            return;
        }

        if(month.isEmpty()) {
            Toast.makeText(this, "Please enter card expiring month", Toast.LENGTH_SHORT).show();
            return;
        }

        if(year.isEmpty()) {
            Toast.makeText(this, "Please enter card expiring year", Toast.LENGTH_SHORT).show();
            return;
        }

        if(date.isEmpty()) {
            Toast.makeText(this, "Please select order date", Toast.LENGTH_SHORT).show();
            return;
        }

        if(time.isEmpty()) {
            Toast.makeText(this, "Please select order time", Toast.LENGTH_SHORT).show();
            return;
        }

        if(cvc.isEmpty()) {
            Toast.makeText(this, "Please enter CVC / CVV", Toast.LENGTH_SHORT).show();
            return;
        }

        if(cardNumber.length() != 16) {
            Toast.makeText(this, "Please enter a valid card number", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Integer.parseInt(month) > 12) {
            Toast.makeText(this, "Please enter a valid month ", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Integer.parseInt(month) < 1) {
            Toast.makeText(this, "Please enter a valid month ", Toast.LENGTH_SHORT).show();
            return;
        }

        if(cvc.length() != 3) {
            Toast.makeText(this, "Please enter a valid CVC / CVV", Toast.LENGTH_SHORT).show();
            return;
        }

        makeOrder(customerName, customerPhone, product_name, product_price, product_category, product_image, date, time, count);
    }

    // method to make order
    private void makeOrder(String customerName, String customerPhone, String product_name, String product_price, String product_category, String product_image, String date, String time, int count) {

        // calculate total price
        float actualPrice = Float.parseFloat(product_price);
        float totalPrice = actualPrice * count;

        // prepare data
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("customer_id", auth.getCurrentUser().getUid());
        orderData.put("customer_name", customerName);
        orderData.put("customer_phone", customerPhone);
        orderData.put("sweet_name", product_name);
        orderData.put("sweet_price", String.valueOf(totalPrice));
        orderData.put("sweet_category", product_category);
        orderData.put("sweet_count", String.valueOf(count));
        orderData.put("sweet_image", product_image);
        orderData.put("order_date", date);
        orderData.put("order_time", time);

        // dialog
        final ProgressDialog dialog = ProgressDialog.show(this, "Making order", "Please wait...", false, false);

        // add to database
        db.collection("orders").document().set(orderData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                if(task.isSuccessful()) {
                    Toast.makeText(PaymentActivity.this, "Your order has been done successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PaymentActivity.this, "Cannot process your order", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
