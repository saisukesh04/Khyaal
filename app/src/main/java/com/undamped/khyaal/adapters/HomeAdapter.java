package com.undamped.khyaal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.undamped.khyaal.R;
import com.undamped.khyaal.database.MedDatabase;
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

        holder.whole_med_card.setOnLongClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to delete this medicine")
                    .setPositiveButton("Ok", (paramDialogInterface, paramInt) -> {
                        MedDatabase.getInstance(context).medDao().deleteMed(med);
                        medicines.remove(position);
                        notifyDataSetChanged();
                    }).setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(context,"Medicine NOT deleted", Toast.LENGTH_LONG).show())
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return medicines != null? medicines.size(): 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView medicineName, medicineDose, medicineDays;
        private CardView whole_med_card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            medicineName = itemView.findViewById(R.id.medicineName);
            medicineDose = itemView.findViewById(R.id.medicineDose);
            medicineDays = itemView.findViewById(R.id.medicineDays);
            whole_med_card = itemView.findViewById(R.id.whole_med_card);
        }
    }
}
