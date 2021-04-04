package com.undamped.khyaal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.undamped.khyaal.R;
import com.undamped.khyaal.entity.Medicine;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private Context context;
    private List<Medicine> medicines;

    public HomeAdapter(List<Medicine> medicines) {
        this.medicines = medicines;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.home_medicine_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicine med = medicines.get(position);
        holder.medicineDays.setText(med.getDays() + " days");
        holder.medicineDose.setText(med.getDosage());
        holder.medicineName.setText(med.getName());
    }

    @Override
    public int getItemCount() {
        return medicines != null? medicines.size(): 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView medicineName, medicineDose, medicineDays;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            medicineName = itemView.findViewById(R.id.medicineName);
            medicineDose = itemView.findViewById(R.id.medicineDose);
            medicineDays = itemView.findViewById(R.id.medicineDays);
        }
    }
}
