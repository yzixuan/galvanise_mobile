package com.example.zee.galvanisemobile;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CartActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView cartQuantity;
    private TextView totalPayable;
    private TextView discountedPayable;
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        cartQuantity = (TextView)findViewById(R.id.cart_quantity);
        totalPayable = (TextView)findViewById(R.id.total_payable);
        discountedPayable = (TextView)findViewById(R.id.discounted_payable);
        checkoutButton = (Button)findViewById(R.id.checkout);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refreshCartInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setCheckoutVisibility () {
        // empty cart
        if (ShoppingCart.getTotalPrice() <= 0) {
            discountedPayable.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.GONE);
        }
        else {
            // cart is not empty
            // check if discount is applicable
            if (ShoppingCart.getDiscount() > 0) {
                discountedPayable.setVisibility(View.VISIBLE);
                discountedPayable.setText("Total Payable (after 20% discount): SGD $" + String.format("%.2f", ShoppingCart.getDiscountedPrice()));
            }
            // make checkout button visible
            checkoutButton.setVisibility(View.VISIBLE);
            checkoutButton.setText("Checkout and Pay (SGD $" + String.format("%.2f", ShoppingCart.getDiscountedPrice()) + ")");
        }

    }

    public void refreshCartInfo() {
        cartQuantity.setText("Total No. of Items in Cart: " + ShoppingCart.getNumOfItems());
        totalPayable.setText("Cart Subtotal: SGD $" + String.format("%.2f", ShoppingCart.getTotalPrice()));
        setCheckoutVisibility();
    }

    public void onClick_checkout(View view) {
        Toast.makeText(this, "to be implemented", Toast.LENGTH_SHORT).show();
    }
}
