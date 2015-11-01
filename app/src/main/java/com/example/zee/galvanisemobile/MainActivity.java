package com.example.zee.galvanisemobile;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.example.zee.galvanisemobile.tabs.SlidingTabLayout;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private NavigationDrawerFragment drawerFragment;
    private Toolbar toolbar;
    private BeaconManager beaconManager;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();
        setNavigationDrawer();
        setTabs();

        setUpBeaconManager();
    }

    public void setToolbar() {

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setNavigationDrawer() {

        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

    }

    public void setTabs() {

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(new MenuPagerAdapter(getSupportFragmentManager()));
        mTabs = (SlidingTabLayout)findViewById(R.id.tabs);
        mTabs.setViewPager(mPager);
    }

    @Override
    public void onNewIntent(Intent intent){
        Bundle extras = intent.getExtras();
        if(extras != null){
            if(extras.containsKey("NotificationMessage")) {
                if (ShoppingCart.getDiscount() >= 0.2) {
                    toastDiscount(ShoppingCart.getDiscount());
                }
                else {

                    handleBeaconDialog();
                }
            }
        }
    }

    private void toastDiscount(double discount) {
        Toast.makeText(this, "A " + Math.round(discount * 100) + "% discount has been included", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            startShareActivity("Galvanise Cafe", getResources().getString(R.string.social_share_text));
        }

        if (id == R.id.action_cart) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUpBeaconManager() {
        beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region(
                        "monitored region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                        31629, 43111));
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                showNotification(
                        "Welcome to Galvanise Cafe",
                        "Check-in to get 20% off your bill");
                handleBeaconDialog();
            }

            @Override
            public void onExitedRegion(Region region) {

            }
        });
    }

    public void showNotification(String title, String message) {

        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notifyIntent.putExtra("NotificationMessage", "world");

        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_logo_white)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notification);
    }

    public void handleBeaconDialog() {

        if (ShoppingCart.getDiscount() <= 0) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_beacon_promo);
            dialog.setTitle("Welcome to Galvanise!");

            Button okButton = (Button) dialog.findViewById(R.id.button_ok);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingCart.setDiscount(0.2);
                    toastDiscount(0.2);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private void startShareActivity(String subject, String text) {
        try {
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(android.content.Intent.EXTRA_TEXT, text);
            startActivity(intent);
        }
        catch(android.content.ActivityNotFoundException e) {

        }
    }

    class MenuPagerAdapter extends FragmentPagerAdapter {

        String[] tabs;

        public MenuPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            FoodMenuFragment menuTabFragment = FoodMenuFragment.getInstance(position);
            return menuTabFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 6;
        }
    }
}
