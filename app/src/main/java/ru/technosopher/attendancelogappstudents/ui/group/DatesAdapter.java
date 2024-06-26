package ru.technosopher.attendancelogappstudents.ui.group;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.technosopher.attendancelogappstudents.databinding.DateElementBinding;


public class DatesAdapter extends RecyclerView.Adapter<DatesAdapter.ViewHolder> {

    private final List<String> data = new ArrayList<>();


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                DateElementBinding.inflate(LayoutInflater.from(
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
    public void update(List<String> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final DateElementBinding binding;

        public ViewHolder(@NonNull DateElementBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String item) {
            binding.dateTvItem.setText(item);
        }
    }

}
