package com.undamped.khyaal;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import static android.app.Activity.RESULT_OK;

public class ScanFragment extends Fragment {

    @BindView(R.id.process_image) Button process_image;
    @BindView(R.id.add_event_photo) CardView add_event_photo;
    @BindView(R.id.prescriptionImageView) ImageView prescriptionImageView;
    @BindView(R.id.imageinfo) TextView imageinfo;

    final public static int IMAGE_CODE = 1;
    private Uri imageUri;                           // URI of the image to be processed
    private ArrayList<String> prescription;
    String scanText;

    public ScanFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scan, container, false);

        ButterKnife.bind(this, root);

        add_event_photo.setOnClickListener(view -> {
            selectImage();
        });

        process_image.setOnClickListener(view -> {
            if(imageUri != null){
                try {
                    processImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_CODE);
    }

    private void processImage() throws IOException {
        scanText = "";
        prescription = new ArrayList<>();
        TextRecognizer recognizer = TextRecognition.getClient();
        InputImage inputImage = InputImage.fromFilePath(getContext(), imageUri);
        Task<Text> result = recognizer.process(inputImage).addOnSuccessListener(visionText -> {     // Task completed successfully
            for (Text.TextBlock block : visionText.getTextBlocks()) {
                Rect boundingBox = block.getBoundingBox();
                Point[] cornerPoints = block.getCornerPoints();
                String text = block.getText();
                scanText += text;
                Log.e("Info", text);
                prescription.add(text);
                //for (Text.Line line: block.getLines()) {
                // ...
                //  for (Text.Element element: line.getElements()) {
                // ...
                //}
                //}
            }
            imageinfo.setText(scanText);
        }).addOnFailureListener(e -> {                      // Task failed with an exception
            Log.e("Error", e.getMessage());
        });                                                 // [END run_detector]
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            Glide.with(getContext()).load(imageUri).into(prescriptionImageView);
        } else {
            Toast.makeText(getContext(), "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }
}