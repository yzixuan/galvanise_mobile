package com.example.zee.galvanisemobile;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SharedPreferences preferences;
    private EditText phoneNumberInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setToolbar();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        phoneNumberInput = (EditText)findViewById(R.id.phoneNumber);

        setPhoneNumberInput();
    }

    public void setToolbar() {

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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

    public void setPhoneNumberInput() {

        String previouslySaved = preferences.getString("PhoneNumber", "");
        phoneNumberInput.setText(previouslySaved, TextView.BufferType.EDITABLE);
    }

    public void onClick_saveSettings(View view) {

        String phoneNumber = getPhoneNumberInput();

        if (phoneNumber.isEmpty() || phoneNumber == null) {

            Toast.makeText(this, "Phone Number should not be empty.", Toast.LENGTH_LONG).show();

        } else {
            savePhoneNumber(phoneNumber);
        }
    }

    public String getPhoneNumberInput() {

        return phoneNumberInput.getText().toString().trim();
    }

    public void savePhoneNumber(String phoneNumber) {

        String sgPhoneNumber = phoneNumber;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PhoneNumber", sgPhoneNumber);
        editor.apply();

        Toast.makeText(this, "SMS settings have been saved.", Toast.LENGTH_LONG).show();
    }
}
