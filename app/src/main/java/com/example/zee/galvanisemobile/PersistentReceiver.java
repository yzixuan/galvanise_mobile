package com.example.zee.galvanisemobile;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.Toast;

public class PersistentReceiver extends BroadcastReceiver {

    Context context;

    public PersistentReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        Bundle extras = intent.getExtras();

        if(intent.getAction().equals(Intent.ACTION_BATTERY_LOW) || intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            showNotification("Your battery is low", "Get a charger from the counter at Galvanise Cafe!");
        }

        if(extras != null){
            if(extras.containsKey("AddtoCart")) {
                boolean broadcast = intent.getBooleanExtra("AddtoCart", false);
                if (broadcast == true) {
                    Toast.makeText(context, "Successfully added to cart.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void showNotification(String title, String message) {

        Intent notifyIntent = new Intent(context, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivities(context, 0,
                new Intent[] { notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(context)
                .setSmallIcon(android.R.drawable.ic_lock_idle_low_battery)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notification);
    }
}