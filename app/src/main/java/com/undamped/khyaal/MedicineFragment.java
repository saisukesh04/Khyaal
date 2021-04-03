package com.undamped.khyaal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.undamped.khyaal.adapters.HomeAdapter;
import com.undamped.khyaal.database.MedDao;
import com.undamped.khyaal.database.MedDatabase;
import com.undamped.khyaal.entity.Medicine;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedicineFragment extends Fragment {

    @BindView(R.id.medicineRecyclerView) RecyclerView medicineRecyclerView;
    @BindView(R.id.no_medicines_text) TextView no_medicines_text;

    public MedicineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_medicine, container, false);

        ButterKnife.bind(this, root);

        MedDao medDao = MedDatabase.getInstance(getContext()).medDao();

        List<Medicine> meds = medDao.loadAllMedicines();
        Log.e("Info", String.valueOf(meds));

        HomeAdapter hAdapter = new HomeAdapter(meds);
        medicineRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        medicineRecyclerView.setAdapter(hAdapter);

        if (meds.size() == 0)
            no_medicines_text.setVisibility(View.VISIBLE);

        return root;
    }
}