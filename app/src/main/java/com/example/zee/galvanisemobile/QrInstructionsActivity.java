package com.example.zee.galvanisemobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QrInstructionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_instructions);

        checkForRescan();
    }

    // if user is just re-scanning table number, skip instructions and go
    // straight to using ZXing scanner
    public void checkForRescan() {

        Intent intent = getIntent();
        boolean rescan = intent.getBooleanExtra("rescanCode", false);

        if (rescan) {
            openZXingScanner();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qr_instructions, menu);
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


    public void onClick_scanQRCode(View view) {

        openZXingScanner();
    }

    // use ZXing Library to scan QR Code
    public void openZXingScanner() {

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(ScanQRCodeActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setOrientationLocked(true);
        integrator.setPrompt("Scan the table number");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    // wait for QR Code result from ZXing (required for integrating ZXing)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult barcodeResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (barcodeResult != null) {
            handleTableNumberScanned(barcodeResult.getContents());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // if it's a valid table number, go to the next activity
    // else, alert the user to try again
    private void handleTableNumberScanned(String qrCode) {

        if (qrCode == null) {
            finish();
        }
        else if (qrCode.matches("[-+]?\\d*\\.?\\d+")) {

            Intent returnIntent = new Intent(this, CartActivity.class);
            returnIntent.putExtra("tableQRCode", qrCode);
            setResult(RESULT_OK, returnIntent);
            finish();

        } else {

            new AlertDialog.Builder(this)
                    .setTitle("Invalid Table Number")
                    .setCancelable(false)
                    .setMessage("Sorry, are you sure you're seated inside Galvanise Cafe? please try scanning your table number's QR Code again.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }

    }

}
