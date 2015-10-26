package com.example.zee.galvanisemobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CartActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView cartQuantity;
    private TextView totalPayable;
    private TextView discountedPayable;
    private Button checkoutButton;
    private LinearLayout tableNumberLayout;
    private TextView tableNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartQuantity = (TextView)findViewById(R.id.cart_quantity);
        totalPayable = (TextView)findViewById(R.id.total_payable);
        discountedPayable = (TextView)findViewById(R.id.discounted_payable);
        checkoutButton = (Button)findViewById(R.id.checkout);

        setToolbar();
        handleTableNumber();
        refreshCartInfo();
    }

    public void setToolbar() {

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void handleTableNumber() {

        tableNumberLayout = (LinearLayout)findViewById(R.id.tableNumberLayout);

        if (ShoppingCart.getTableNumber() == null) {

            tableNumberLayout.setVisibility(View.GONE);

        } else {

            tableNumberTextView = (TextView)findViewById(R.id.tableNumber);
            tableNumberTextView.setText(ShoppingCart.getTableNumber());
            tableNumberLayout.setVisibility(View.VISIBLE);
        }
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
                discountedPayable.setText("Total Payable (after " + Math.round(ShoppingCart.getDiscount() * 100) + "% discount): SGD $" + String.format("%.2f", ShoppingCart.getDiscountedPrice()));
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

        if (ShoppingCart.getTableNumber() == null) {
            Intent intent = new Intent(this, QrInstructionsActivity.class);
            startActivityForResult(intent, 1);
        } else {
            Intent intent = new Intent(this, PrePayPalActivity.class);
            startActivity(intent);
        }

    }

    public void onClick_rescanQRCode(View view) {

        Intent intent = new Intent(this, QrInstructionsActivity.class);
        intent.putExtra("rescanCode", true);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if(resultCode == QrInstructionsActivity.RESULT_OK){

                ShoppingCart.setTableNumber(data.getStringExtra("tableQRCode"));
                handleTableNumber();
            }

            if (resultCode == QrInstructionsActivity.RESULT_CANCELED) {

            }
        }
    }

}
