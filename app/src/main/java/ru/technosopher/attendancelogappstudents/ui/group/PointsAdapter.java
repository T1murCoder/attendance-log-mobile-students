package ru.technosopher.attendancelogappstudents.ui.group;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.databinding.ElementPointBinding;
import ru.technosopher.attendancelogappstudents.domain.entities.AttendanceEntity;
import ru.technosopher.attendancelogappstudents.ui.utils.OnChangeText;


public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.ViewHolder> {

    private final Context context;
    private final List<AttendanceEntity> data = new ArrayList<>();

    public PointsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ElementPointBinding.inflate(LayoutInflater.from(
                                parent.getContext()),
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void update(List<AttendanceEntity> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ElementPointBinding binding;

        public ViewHolder(@NonNull ElementPointBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AttendanceEntity item) {
            Integer point = Math.round(Float.valueOf(item.getPoints()));
            if (point == 0) binding.pointEt.setHint(String.valueOf(point));
            else binding.pointEt.setText(String.valueOf(point));
        }
    }
}
