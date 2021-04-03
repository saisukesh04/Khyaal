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
    List<String> medicines, doses, duration;

    public MedicineAdapter(List<String> medicines, List<String> doses, List<String> duration) {
        this.medicines = medicines;
        this.doses = doses;
        this.duration = duration;
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
        holder.dosage_text.setText(doses.get(position));
        holder.duration_days.setText(duration.get(position));
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
