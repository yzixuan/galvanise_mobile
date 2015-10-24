package com.example.zee.galvanisemobile;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import java.util.Iterator;

public class SuccessfulPaymentActivity extends AppCompatActivity {

    private TextView paymentDateTime;
    private TextView cartSubtotal;
    private TextView totalPayable;
    private ReceiptItem receipt;
    private SharedPreferences preferences;
    private String dateOfOrder = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_payment);

        setPaymentDateTime();
        generateReceiptObject();
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

    private void generateReceiptObject() {
        Calendar calendar = Calendar.getInstance();
        receipt = new ReceiptItem(calendar.getTime());

        cartSubtotal = (TextView)findViewById(R.id.cart_subtotal);
        totalPayable = (TextView)findViewById(R.id.total_payable);
        cartSubtotal.setText("Cart Subtotal: SGD $"+ String.format("%.2f", receipt.getSubTotal()));
        totalPayable.setText("Total Paid (incl discounts): SGD $" + String.format("%.2f", receipt.getTotalPaid()));

        FinalisedCartFragment fragment = (FinalisedCartFragment) getFragmentManager().findFragmentById(R.id.finalized_fragment_cart);
        fragment.modifyAdapterContents(receipt);

        sendSMSReceipt();
    }

    private void setPaymentDateTime() {
        paymentDateTime = (TextView)findViewById(R.id.receipt_date_time);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm aaa");
        dateOfOrder = df.format(calendar.getTime());

        paymentDateTime.setText("on " + dateOfOrder);
    }

    /*
     * NOTE: We are not able to use third-party messaging clients such as Twilio to send SMSes
     * because it costs money. Hence, we are just using the default SMS Manager as a proof of concept.
     */
    private void sendSMSReceipt() {

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean smsPermission = preferences.getBoolean("SMSPermission", false);
        String savedNumber = preferences.getString("PhoneNumber", "");

        // if user has indicated permission to send SMS in the settings activity, then send SMS
        // else, no SMS will be sent to the user.
        if (smsPermission == true && !savedNumber.isEmpty()) {

            String receiptMessage = composeSMSReceipt();

            PendingIntent sentPI;
            String SENT = "SMS_SENT";
            sentPI = PendingIntent.getBroadcast(this, 0,new Intent(SENT), 0);

            try {

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(savedNumber, null, receiptMessage, sentPI, null);
                Toast.makeText(getApplicationContext(), "Sending SMS to " + savedNumber, Toast.LENGTH_LONG).show();

            } catch (Exception e) {

                Toast.makeText(getApplicationContext(), "No SMS was sent. Your phone number may have been invalid. Please check settings." + savedNumber,
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }
        }
    }

    private String composeSMSReceipt() {

        String title = "Galvanise Cafe Receipt";
        String dateTime = dateOfOrder;

        String orderItemsText = title.concat("\n" + dateTime + "\n\n");

        /*for(OrderItem currOrder: receipt.getOrderItems()){

            String itemName = currOrder.getMenuItem().getItemName();
            String quantity = String.valueOf(currOrder.getQuantity());
            String price = String.format("%.2f", currOrder.getMenuItem().getPromoPrice());

            String currItemText = itemName + " :\n($" + price + ") x " + quantity + "\n";
            orderItemsText = orderItemsText.concat(currItemText);

        }*/

        String subtotal = String.format("%.2f", receipt.getSubTotal());
        String discount = String.valueOf(Math.round(receipt.getDiscount() * 100));
        String totalPaid = String.format("%.2f", receipt.getTotalPaid());

        String overallPayment = "Cart Subtotal = $".concat(subtotal + "\n");
        overallPayment = overallPayment.concat("Discount: " + discount + "%\n");
        overallPayment = overallPayment.concat("Total Paid = $" + totalPaid);

        return orderItemsText.concat(overallPayment);
    }

    private void clearCart() {
        ShoppingCart.clear();
    }

    public void onClick_goMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
