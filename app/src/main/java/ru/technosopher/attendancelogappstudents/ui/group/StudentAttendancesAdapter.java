package ru.technosopher.attendancelogappstudents.ui.group;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.databinding.StudentTableItemBinding;
import ru.technosopher.attendancelogappstudents.domain.entities.AttendanceEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.StudentEntity;


public class StudentAttendancesAdapter extends RecyclerView.Adapter<StudentAttendancesAdapter.ViewHolder> {
    private final List<StudentEntity> data = new ArrayList<>();

    private boolean state = true;
    private final Context context;
    public StudentAttendancesAdapter(Context context, boolean state) {
        this.context = context;
        this.state = state;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                StudentTableItemBinding.inflate(LayoutInflater.from(
                                parent.getContext()),
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(data.get(position), state);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<StudentEntity> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateState(boolean newState) {
        this.state = newState;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final StudentTableItemBinding binding;

        public ViewHolder(@NonNull StudentTableItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(StudentEntity item, boolean att) {
            binding.tableStudentName.setText(item.getFullName());
            if (att) {
                CheckBoxAdapter adapter = new CheckBoxAdapter(context);
                binding.attAndBallsRv.setAdapter(adapter);
                adapter.update(item.getAttendanceEntityList());
            } else {
                PointsAdapter adapter = new PointsAdapter(context);
                binding.attAndBallsRv.setAdapter(adapter);
                adapter.update(item.getAttendanceEntityList());
            }
        }
    }
}
