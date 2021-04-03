package com.undamped.khyaal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottomNavigation) BottomNavigationView bottomNavigation;
    @BindView(R.id.nav_host_frame) FrameLayout nav_host_frame;
    @BindView(R.id.scanFloatingBtn) FloatingActionButton scanFloatingBtn;
    @BindView(R.id.toolbar2) Toolbar main_toolbar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        scanFloatingBtn.setOnClickListener(view -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_frame, new ScanFragment()).commit();
        });

        bottomNavigation.setSelectedItemId(R.id.action_medicines);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_frame, new MedicineFragment()).commit();

        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_frame, new ProfileFragment()).commit();
                    return true;

                case R.id.action_medicines:
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_frame, new MedicineFragment()).commit();
                    return true;
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }


/*
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
    }

    private void setTapAction() {
        // Create an explicit intent for an Activity in your app
//        Intent intent = new Intent(this, AlertDetails.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.notification_icon)
//                .setContentTitle("My notification")
//                .setContentText("Hello World!")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                // Set the intent that will fire when the user taps the notification
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(false);
    }

    private void processTextBlock() {
        ArrayList<String> toBeNotified = this.prescription;
        for (String s : toBeNotified) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "0")
                    .setSmallIcon(R.drawable.taking_care)
                    .setContentTitle("Notification boss")
                    .setContentText(s)
                    .setPriority(NotificationCompat.PRIORITY_MAX);
            /*
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(notificationId, builder.build());

        }
    }
    */
}