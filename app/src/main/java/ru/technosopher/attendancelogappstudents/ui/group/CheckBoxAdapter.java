package ru.technosopher.attendancelogappstudents.ui.group;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.databinding.ElementCheckBoxBinding;
import ru.technosopher.attendancelogappstudents.domain.entities.AttendanceEntity;


public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxAdapter.ViewHolder>{
    private final Context context;
    private final List<AttendanceEntity> data = new ArrayList<>();

    public CheckBoxAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ElementCheckBoxBinding.inflate(LayoutInflater.from(
                                parent.getContext()),
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void update(List<AttendanceEntity> newData){
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ElementCheckBoxBinding binding;
        public ViewHolder(@NonNull ElementCheckBoxBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AttendanceEntity item) {
            binding.cbElementCheckbox.setChecked(item.getVisited());

        }

    }
}
