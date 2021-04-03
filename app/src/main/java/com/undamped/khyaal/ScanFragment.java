package com.undamped.khyaal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.undamped.khyaal.adapters.MedicineAdapter;
import com.undamped.khyaal.database.MedDao;
import com.undamped.khyaal.database.MedDatabase;
import com.undamped.khyaal.entity.Medicine;

import static android.app.Activity.RESULT_OK;

public class ScanFragment extends Fragment {

    @BindView(R.id.process_image) Button process_image;
    @BindView(R.id.add_event_photo) CardView add_event_photo;
    @BindView(R.id.prescriptionImageView) ImageView prescriptionImageView;
    @BindView(R.id.scannedMedView) RecyclerView scannedMedView;
    @BindView(R.id.confirmMedicineBtn) Button confirmMedicineBtn;

    final public static int IMAGE_CODE = 1;
    private Uri imageUri;                           // URI of the image to be processed
    private ArrayList<String> medicines, dosage, duration;
    MedicineAdapter mAdapter;

    public ScanFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scan, container, false);

        ButterKnife.bind(this, root);

        scannedMedView.setLayoutManager(new LinearLayoutManager(getContext()));

        add_event_photo.setOnClickListener(view -> selectImage());

        process_image.setOnClickListener(view -> {
            if (imageUri != null) {
                try {
                    processImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        confirmMedicineBtn.setOnClickListener(view -> {
            addMedicinesToDb();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_frame, new MedicineFragment()).commit();
        });

        return root;
    }

    private void addMedicinesToDb() {
        MedDao medDb = MedDatabase.getInstance(getContext()).medDao();
        for(int i=0;i<medicines.size();i++) {
            Medicine medicine = new Medicine();
            medicine.setName(medicines.get(i));
            medicine.setDays(Integer.parseInt(duration.get(i).replaceAll("[^0-9]", "")));
            String dose =  dosage.get(i);
            if (dose.charAt(0) == '1')
                medicine.setMorning(true);
            if (dose.charAt(2) == '1')
                medicine.setAfternoon(true);
            if (dose.charAt(4) == '1')
                medicine.setEvening(true);
            medDb.insertMed(medicine);
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_CODE);
    }

    private void processImage() throws IOException {
        medicines = new ArrayList<>();
        dosage = new ArrayList<>();
        duration = new ArrayList<>();
        TextRecognizer recognizer = TextRecognition.getClient();
        InputImage inputImage = InputImage.fromFilePath(getContext(), imageUri);
        Task<Text> result = recognizer.process(inputImage).addOnSuccessListener(visionText -> {     // Task completed successfully
            Log.e("Info: Block size", visionText.getTextBlocks().size() + " Size");
            List<Text.TextBlock> blocks = visionText.getTextBlocks();
            for (int i = 0; i < blocks.size(); i += 2) {
//            for (Text.TextBlock block : visionText.getTextBlocks()) {
//                Rect boundingBox = block.getBoundingBox();
//                Point[] cornerPoints = block.getCornerPoints();
                String[] dose_days = blocks.get(i + 1).getText().split("x");
                medicines.add(blocks.get(i).getText());
                dosage.add(dose_days[0].trim());
                duration.add(dose_days[1].trim());
                Log.e("Medicine " + i / 2, blocks.get(i).getText() + ", dose: " + blocks.get(i + 1).getText());
            }
            mAdapter = new MedicineAdapter(medicines, dosage, duration);
            scannedMedView.setItemViewCacheSize(medicines.size());
            scannedMedView.setAdapter(mAdapter);
            confirmMedicineBtn.setEnabled(true);
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