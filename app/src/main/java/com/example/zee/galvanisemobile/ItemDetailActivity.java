package com.example.zee.galvanisemobile;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ItemDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private com.example.zee.galvanisemobile.MenuItem food;

    ImageView imageView;
    TextView foodNameText;
    TextView priceLabel;
    Button addCartButton;
    EditText quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        food = i.getParcelableExtra("foodObject");
        foodNameText = (TextView)findViewById(R.id.foodNameText);
        priceLabel = (TextView)findViewById(R.id.priceText);
        imageView = (ImageView)findViewById(R.id.image);
        addCartButton = (Button)findViewById(R.id.add_to_cart);

        foodNameText.setText(food.getItemName());
        priceLabel.setText("$" + String.format("%.2f", food.getPromoPrice()));

        imageView.setImageResource(food.getThumbnail());

        // add button listener
        addCartButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(ItemDetailActivity.this);
                dialog.setContentView(R.layout.dialog_add_to_cart);
                dialog.setTitle("Select Quantity");

                Button plusButton = (Button) dialog.findViewById(R.id.plus_button);
                Button minusButton = (Button) dialog.findViewById(R.id.minus_button);

                Button confirmAdd = (Button) dialog.findViewById(R.id.confirm_add);
                Button cancel = (Button) dialog.findViewById(R.id.cancel);

                quantity = (EditText)dialog.findViewById(R.id.quantity);

                plusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currValue = Integer.parseInt(quantity.getText().toString());
                        quantity.setText(String.valueOf(currValue+1), TextView.BufferType.EDITABLE);
                    }
                });

                minusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currValue = Integer.parseInt(quantity.getText().toString());
                        if (currValue != 1) {
                            quantity.setText(String.valueOf(currValue-1), TextView.BufferType.EDITABLE);
                        }
                    }
                });

                confirmAdd.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        // create order item and add to shopping cart
                        OrderItem orderItem = new OrderItem(food, Integer.parseInt(quantity.getText().toString()));
                        ShoppingCart.addOrderItem(orderItem);

                        sendPersistentBroadcastMessage();

                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_detail, menu);
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

        if (id == R.id.action_cart) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        }

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendPersistentBroadcastMessage() {
        Intent myIntent = new Intent("GalvaniseBroadcast");
        myIntent.putExtra("AddtoCart", true);
        sendBroadcast(myIntent);
    }

}
