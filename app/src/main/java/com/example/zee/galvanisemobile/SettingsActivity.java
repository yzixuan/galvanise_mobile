package com.example.zee.galvanisemobile;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private Switch aSwitch;
    private Toolbar toolbar;
    private SharedPreferences preferences;
    private EditText phoneNumberInput;
    private LinearLayout phoneInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setToolbar();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        phoneNumberInput = (EditText)findViewById(R.id.phoneNumber);
        aSwitch = (Switch)findViewById(R.id.switch1);
        phoneInputLayout = (LinearLayout)findViewById(R.id.phoneInputLayout);

        getPreferencesSettings();
        listenSwitch();
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

    public void getPreferencesSettings() {

        boolean smsPermission = preferences.getBoolean("SMSPermission", false);
        String previouslySaved = preferences.getString("PhoneNumber", "");
        phoneNumberInput.setText(previouslySaved, TextView.BufferType.EDITABLE);

        if (smsPermission) {

            aSwitch.setChecked(true);

        } else {

            aSwitch.setChecked(false);
            phoneInputLayout.setVisibility(View.INVISIBLE);

        }
    }

    public void onClick_saveSettings(View view) {

        saveSMSPermission(aSwitch.isChecked());

        if (aSwitch.isChecked()) {

            getPhoneNumberInput();

        } else {
            Toast.makeText(this, "SMS settings have been saved.", Toast.LENGTH_LONG).show();
        }
    }

    public void getPhoneNumberInput() {

        String phoneNumber =  phoneNumberInput.getText().toString().trim();

        if (phoneNumber.isEmpty() || phoneNumber == null) {

            Toast.makeText(this, "Phone Number should not be empty.", Toast.LENGTH_LONG).show();

        } else {
            savePhoneNumber(phoneNumber);
            Toast.makeText(this, "SMS settings have been saved.", Toast.LENGTH_LONG).show();
        }
    }

    public void saveSMSPermission(boolean canSend) {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("SMSPermission", canSend);
        editor.apply();
    }

    public void savePhoneNumber(String phoneNumber) {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PhoneNumber", phoneNumber);
        editor.apply();
    }

    public void listenSwitch() {
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    phoneInputLayout.setVisibility(View.VISIBLE);
                }else{
                    phoneInputLayout.setVisibility(View.INVISIBLE);
                }

            }
        });
    }
}
