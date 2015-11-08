package com.example.zee.galvanisemobile.payment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.zee.galvanisemobile.MainActivity;
import com.example.zee.galvanisemobile.R;
import com.example.zee.galvanisemobile.cart.ShoppingCart;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.ShippingAddress;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PrePayPalActivity extends AppCompatActivity {

    private static final String TAG = "paymentExample";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    // PayPal client ID
    private static final String CONFIG_CLIENT_ID = "AT2SexMbi7MsC4b2Ibcy7fDbfjhd_GFmhCu8QGq8VhAFO6MNfyKFT9-XPAjFeywfU9J1akayou-BXP-i";

    private static final int REQUEST_CODE_PAYMENT = 1;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_pay_pal);

        if (ShoppingCart.getTotalPrice() == 0) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        goPayPalGateway();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pre_pay_pal, menu);
        return true;
    }

    @Override
    protected void onResume() {

        if (ShoppingCart.getTotalPrice() == 0) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        super.onResume();
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

    public void goPayPalGateway() {

        PayPalPayment confirmedOrder = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
        addAppProvidedShippingAddress(confirmedOrder);
        enableShippingAddressRetrieval(confirmedOrder, true);
        Intent intent = new Intent(PrePayPalActivity.this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, confirmedOrder);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal(ShoppingCart.getDiscountedPrice()), "SGD", "Galvanise Cafe Order",
                paymentIntent);
    }

    private void addAppProvidedShippingAddress(PayPalPayment paypalPayment) {
        ShippingAddress shippingAddress =
                new ShippingAddress().recipientName("Galvanise Cafe").line1("175 Bencoolen St, #01-53 Burlington Square")
                        .city("Singapore").state("Singapore").postalCode("78729").countryCode("SG");
        paypalPayment.providedShippingAddress(shippingAddress);
    }

    // Enable retrieval of shipping addresses from buyer's PayPal account
    private void enableShippingAddressRetrieval(PayPalPayment paypalPayment, boolean enable) {
        paypalPayment.enablePayPalShippingAddressesRetrieval(enable);
    }

    private PayPalOAuthScopes getOauthScopes() {

        // create the set of required scopes
        Set<String> scopes = new HashSet<String>(
                Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS) );
        return new PayPalOAuthScopes(scopes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {

            if (resultCode == Activity.RESULT_OK) {

                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirm != null) {

                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));

                        // go the successful payment activity once order is successful
                        Intent intent = new Intent(this, SuccessfulPaymentActivity.class);
                        startActivity(intent);

                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

                Log.i(TAG, "The user canceled.");
                finish();

            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {

                Log.i(TAG, "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        // Stop PayPal service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}
