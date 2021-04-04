package com.undamped.khyaaldoctor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.scan_barcode) ImageView scan_barcode;
    @BindView(R.id.fetchBtn) Button fetchBtn;
    @BindView(R.id.constraintLayout) ConstraintLayout constraintLayout;
    @BindView(R.id.patientID) EditText patientID;
    @BindView(R.id.prescribeBtn) Button prescribeBtn;
    @BindView(R.id.patName) TextView patName;
    @BindView(R.id.invalidText) TextView invalidText;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.editTextMedicine) EditText editTextMedicine;
    @BindView(R.id.editTextDose) EditText editTextDose;
    @BindView(R.id.editTextDays) EditText editTextDays;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Users");

        scan_barcode.setOnClickListener(view -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.setPrompt("Scan a Patient's QR Code");
            intentIntegrator.setOrientationLocked(false);
            intentIntegrator.initiateScan();
        });

        fetchBtn.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            View focusedView = MainActivity.this.getCurrentFocus();
            if (focusedView != null) {
                imm.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            invalidText.setVisibility(View.INVISIBLE);
            constraintLayout.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            mRef.child(patientID.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        patName.setText(snapshot.child("Name").getValue().toString());
                        constraintLayout.setVisibility(View.VISIBLE);
                    } else {
                        invalidText.setVisibility(View.VISIBLE);
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Snackbar.make(view, "An error occurred. Please try again!", Snackbar.LENGTH_LONG).show();
                    Log.e("Error: MainActivity", error.getMessage());
                }
            });
        });

        prescribeBtn.setOnClickListener(view -> {
            String medName = editTextMedicine.getText().toString();
            String dose = editTextDose.getText().toString();
            String days = editTextDays.getText().toString();
            if (medName.isEmpty() || dose.isEmpty() || days.isEmpty())
                Snackbar.make(view, "Please fill all the fields!!!", Snackbar.LENGTH_LONG).show();
            else {
                progressBar.setVisibility(View.VISIBLE);
                Map<String, String> medMap = new HashMap<>();
                medMap.put("name", medName);
                medMap.put("days", days);
                medMap.put("dose", dose);
                mRef.child(patientID.getText().toString()).child("Medicines")
                        .child(medName + days + (new Random().nextInt(20)))
                        .setValue(medMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        constraintLayout.setVisibility(View.INVISIBLE);
                        patientID.setText("");
                        Snackbar.make(view, "Medicine Prescribed to the Patient!", Snackbar.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                });

            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Scan cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set the content and format of scan message
                patientID.setText(intentResult.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}