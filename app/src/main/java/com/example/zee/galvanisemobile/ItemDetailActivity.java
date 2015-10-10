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

        foodNameText.setText(food.getName());
        priceLabel.setText("$" + String.format("%.2f", food.getPromoPrice()));

        imageView.setImageResource(food.getThumbnail());

        // add button listener
        addCartButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(ItemDetailActivity.this);
                dialog.setContentView(R.layout.dialog_add_to_cart);
                dialog.setTitle("Select Quantity");

                Button confirmAdd = (Button) dialog.findViewById(R.id.confirm_add);
                Button cancel = (Button) dialog.findViewById(R.id.cancel);

                confirmAdd.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        quantity = (EditText)dialog.findViewById(R.id.quantity);

                        // create order item and add to shopping cart
                        OrderItem orderItem = new OrderItem(food, Integer.parseInt(quantity.getText().toString()));
                        ShoppingCart.addOrderItem(orderItem);

                        Toast.makeText(ItemDetailActivity.this, "Successfully added to cart.", Toast.LENGTH_SHORT).show();

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

}
