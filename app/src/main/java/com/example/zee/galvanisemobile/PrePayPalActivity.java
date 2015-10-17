package com.example.zee.galvanisemobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class PrePayPalActivity extends AppCompatActivity {

    private TextView tableNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_pay_pal);

        Intent intent = getIntent();
        String tableNumberFromCode = intent.getStringExtra("tableQRCode");
        tableNumber = (TextView)findViewById(R.id.tableNumber);
        tableNumber.setText(tableNumberFromCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pre_pay_pal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
