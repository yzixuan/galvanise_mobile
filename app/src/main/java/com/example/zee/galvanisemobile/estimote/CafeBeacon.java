package com.example.zee.galvanisemobile.estimote;


import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.example.zee.galvanisemobile.MainActivity;
import com.example.zee.galvanisemobile.R;
import com.example.zee.galvanisemobile.cart.ShoppingCart;

import java.util.List;
import java.util.UUID;

public class CafeBeacon {

    private Context context;
    private BeaconManager beaconManager;

    public CafeBeacon(Context context) {
        this.context = context;
    }

    public void setUpBeaconManager() {
        beaconManager = new BeaconManager(context);

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

        Intent notifyIntent = new Intent(context, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notifyIntent.putExtra("NotificationMessage", "CheckInDiscount");

        PendingIntent pendingIntent = PendingIntent.getActivities(context, 0,
                new Intent[] { notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_logo_white)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notification);
    }

    public void handleBeaconDialog() {

        if (ShoppingCart.getDiscount() <= 0) {
            final Dialog dialog = new Dialog(context);
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

    public void toastDiscount(double discount) {
        Toast.makeText(context, "A " + Math.round(discount * 100) + "% discount has been included", Toast.LENGTH_SHORT).show();
    }

}
