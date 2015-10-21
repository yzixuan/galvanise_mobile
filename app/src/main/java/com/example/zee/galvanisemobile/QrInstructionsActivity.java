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

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(ScanQRCodeActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setOrientationLocked(true);
        integrator.setPrompt("Scan the table number");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult barcodeResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (barcodeResult != null) {
            handleTableNumberScanned(barcodeResult.getContents());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleTableNumberScanned(String qrCode) {
        if (qrCode.matches("[-+]?\\d*\\.?\\d+")) {
            Intent intent = new Intent(this, PrePayPalActivity.class);
            intent.putExtra("tableQRCode", qrCode);
            startActivity(intent);
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
