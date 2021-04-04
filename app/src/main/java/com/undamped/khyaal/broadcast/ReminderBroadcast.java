package com.undamped.khyaal.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.undamped.khyaal.R;


public class ReminderBroadcast extends BroadcastReceiver {

    public ReminderBroadcast(){}

    @Override
    public void onReceive(Context context, Intent intent) {

        String medName = intent.getStringExtra("MedName");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyUs")
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("Time for some medicines")
                .setContentText("Please take " + medName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroup("Reminder")
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}