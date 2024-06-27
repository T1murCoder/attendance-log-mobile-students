package ru.technosopher.attendancelogappstudents.ui.group;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import ru.technosopher.attendancelogappstudents.R;
import ru.technosopher.attendancelogappstudents.databinding.StudentTableItemBinding;
import ru.technosopher.attendancelogappstudents.domain.entities.AttendanceEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.StudentEntity;


public class StudentAttendancesAdapter extends RecyclerView.Adapter<StudentAttendancesAdapter.ViewHolder> {
    private final List<StudentEntity> data = new ArrayList<>();

    private boolean state = true;
    private final Context context;
    private final Consumer<String> onItemClick;
    public StudentAttendancesAdapter(Context context, boolean state, Consumer<String> onItemClick) {
        this.context = context;
        this.state = state;
        this.onItemClick = onItemClick;
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
        holder.bind(data.get(position), state, position);
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

        public void bind(StudentEntity item, boolean att, int position) {
            binding.getRoot().setOnClickListener(v -> {
                onItemClick.accept(item.getId());
            });
            binding.tableStudentName.setText(item.getFullName());
            binding.tvPlace.setText(Integer.toString(position + 1));

            if (position == 0) {
                binding.ivWreath.setVisibility(View.VISIBLE);
                binding.ivWreath.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.laurel_wreath_gold, context.getTheme()));
            } else if (position == 1) {
                binding.ivWreath.setVisibility(View.VISIBLE);
                binding.ivWreath.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.laurel_wreath_silver, context.getTheme()));
            } else if (position == 2) {
                binding.ivWreath.setVisibility(View.VISIBLE);
                binding.ivWreath.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.laurel_wreath_bronze, context.getTheme()));

            } else {
                binding.ivWreath.setVisibility(View.GONE);
            }

            binding.tvPoints.setText(item.getPoints());

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
