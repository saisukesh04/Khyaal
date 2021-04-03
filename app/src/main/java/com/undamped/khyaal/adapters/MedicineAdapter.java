package com.undamped.khyaal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.undamped.khyaal.R;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {

    private Context context;
    List<String> medicines, doses;

    public MedicineAdapter(List<String> medicines, List<String> doses) {
        this.medicines = medicines;
        this.doses = doses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.medicine_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.medicine_name.setText(medicines.get(position));
        String[] dose_days = doses.get(position).split("x");
        holder.dosage_text.setText(dose_days[0].trim());
        holder.duration_days.setText(dose_days[1].trim());
    }

    @Override
    public int getItemCount() {
        return  medicines != null? medicines.size(): 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private EditText medicine_name, dosage_text, duration_days;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            medicine_name = itemView.findViewById(R.id.medicine_name);
            dosage_text = itemView.findViewById(R.id.dosage_text);
            duration_days = itemView.findViewById(R.id.duration_days);
        }
    }
}
