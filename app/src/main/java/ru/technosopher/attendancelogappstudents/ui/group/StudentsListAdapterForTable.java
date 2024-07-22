package ru.technosopher.attendancelogappstudents.ui.group;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.databinding.ItemDummyListBinding;
import ru.technosopher.attendancelogappstudents.domain.entities.StudentEntity;


public class StudentsListAdapterForTable extends RecyclerView.Adapter<StudentsListAdapterForTable.ViewHolder> {
    private final Consumer<String> onItemClick;
    private final List<StudentEntity> data = new ArrayList<>();

    public StudentsListAdapterForTable(Consumer<String> onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ItemDummyListBinding.inflate(LayoutInflater.from(
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
    public void updateData(List<StudentEntity> newData){
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemDummyListBinding binding;
        public ViewHolder(@NonNull ItemDummyListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(StudentEntity item) {
            binding.studentName.setText(item.getFullName());

            binding.getRoot().setOnClickListener(v -> {
                onItemClick.accept(item.getId());
            });
        }
    }
}
