package com.undamped.khyaal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.security.AccessController.getContext;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.add_event_photo) CardView add_event_photo;
    @BindView(R.id.prescriptionImageView) ImageView prescriptionImageView;

    final public static int IMAGE_CODE = 1;
    private Uri imageUri; // URI of the image to be processed
    private ArrayList<String>prescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        add_event_photo.setOnClickListener(view -> {
            selectImage();
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            Glide.with(getApplicationContext()).load(imageUri).into(prescriptionImageView);
           
}


         else {
            Toast.makeText(getApplicationContext(), "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setTapAction(){
          // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, AlertDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);

    }

  
    

    private void processImage(){
        TextRecognizer recognizer = TextRecognition.getClient();
        Task<Text> result =
        recognizer.process(this.imageUri) // To be replaced with data?
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        // Task completed successfully
                        for (Text.TextBlock block : visionText.getTextBlocks()) {
                            Rect boundingBox = block.getBoundingBox();
                            Point[] cornerPoints = block.getCornerPoints();
                            String text = block.getText();
                            prescription.add(text);
                            //for (Text.Line line: block.getLines()) {
                                // ...
                              //  for (Text.Element element: line.getElements()) {
                                    // ...
                                //}
                            //}
                        }
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                // ...
                            }
                        });
// [END run_detector]
    }

    private void processTextBlock(){
        ArrayList<String> toBeNotified = this.prescription;
        for(String s: toBeNotified){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Notification boss")
            .setContentText(s)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            /*
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(notificationId, builder.build());

            */
        }

    }

}