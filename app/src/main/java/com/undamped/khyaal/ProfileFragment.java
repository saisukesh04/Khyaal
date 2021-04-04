package com.undamped.khyaal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.undamped.khyaal.MainActivity.NAME;

public class ProfileFragment extends Fragment {

    @BindView(R.id.logout_btn) Button logout_btn;
    @BindView(R.id.profile_name) TextView profile_name;
    @BindView(R.id.profile_email) TextView profile_email;
    @BindView(R.id.qr_code_image) ImageView qr_code_image;
    @BindView(R.id.profile_image_edit) ImageView profile_image_edit;
    @BindView(R.id.profile_image) CircleImageView profile_image;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, root);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        profile_email.setText(mAuth.getCurrentUser().getEmail());
        profile_name.setText(NAME);

        String qr_text = mAuth.getCurrentUser().getUid();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(qr_text, BarcodeFormat.QR_CODE,400,400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qr_code_image.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        logout_btn.setOnClickListener(view -> {
            mAuth.signOut();
            Snackbar.make(view, "Successfully Signed out", Snackbar.LENGTH_LONG).show();
            Intent logoutIntent = new Intent(getContext(), LoginActivity.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logoutIntent);
            getActivity().finish();
        });

        return root;
    }
}