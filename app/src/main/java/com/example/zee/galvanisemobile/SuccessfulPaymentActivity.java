package com.example.zee.galvanisemobile;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SuccessfulPaymentActivity extends AppCompatActivity {

    private TextView paymentDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_payment);

        setPaymentDateTime();
        //sendSMSReceipt();
        clearCart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_successful_payment, menu);
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

        return super.onOptionsItemSelected(item);
    }

    private void setPaymentDateTime() {
        paymentDateTime = (TextView)findViewById(R.id.receipt_date_time);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm aaa");
        String formattedDate = df.format(calendar.getTime());

        paymentDateTime.setText("on " + formattedDate);
    }

    private void sendSMSReceipt() {

        TelephonyManager telemamanger = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = telemamanger.getLine1Number();
        PendingIntent sentPI;
        String SENT = "SMS_SENT";

        sentPI = PendingIntent.getBroadcast(this, 0,new Intent(SENT), 0);

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+6596374738", null, "Galvanise Cafe", sentPI, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "No SMS was sent. Your phone number may have been invalid. Please check in settings." + phoneNumber,
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }

    private void clearCart() {
        ShoppingCart.clear();
    }

    public void onClick_goMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
