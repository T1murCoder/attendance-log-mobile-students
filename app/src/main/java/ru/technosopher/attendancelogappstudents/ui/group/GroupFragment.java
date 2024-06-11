package ru.technosopher.attendancelogappstudents.ui.group;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ru.technosopher.attendancelogappstudents.R;
import ru.technosopher.attendancelogappstudents.data.source.CredentialsDataSource;
import ru.technosopher.attendancelogappstudents.databinding.FragmentGroupBinding;
import ru.technosopher.attendancelogappstudents.ui.utils.NavigationBarChangeListener;
import ru.technosopher.attendancelogappstudents.ui.utils.UpdateSharedPreferences;

public class GroupFragment extends Fragment {
    public static final String TAG = "GROUP_FRAGMENT";
    private FragmentGroupBinding binding;
    private UpdateSharedPreferences prefs;
    private GroupViewModel viewModel;
    private NavigationBarChangeListener navigationBarChangeListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentGroupBinding.bind(view);
        prefs = (UpdateSharedPreferences) requireContext();
//        Toast.makeText(requireContext(), "OnViewCreated", Toast.LENGTH_SHORT).show();
        Log.d(TAG, prefs.getPrefsId());


        viewModel = new ViewModelProvider(this).get(GroupViewModel.class);

        StudentAttendancesAdapter attendancesAdapter = new StudentAttendancesAdapter(getContext(), true);
        DatesAdapter datesAdapter = new DatesAdapter();


        binding.pointsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.update(prefs.getPrefsId());
                attendancesAdapter.updateState(false);
                attendancesAdapter.updateData(viewModel.getStudents());
                datesAdapter.update(viewModel.extractDates(viewModel.getStudents().get(0).getAttendanceEntityList()));
            }
        });
        binding.attendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.update(prefs.getPrefsId());
                attendancesAdapter.updateState(true);
                attendancesAdapter.updateData(viewModel.getStudents());
                datesAdapter.update(viewModel.extractDates(viewModel.getStudents().get(0).getAttendanceEntityList()));
            }
        });

        binding.studentsRv.setAdapter(attendancesAdapter);
        binding.datesRv.setAdapter(datesAdapter);

        subscribe(viewModel, attendancesAdapter, datesAdapter);
        viewModel.update(prefs.getPrefsId());
    }

    @Override
    public void onStart() {
        super.onStart();
        prefs = (UpdateSharedPreferences) requireContext();
        CredentialsDataSource.getInstance().updateLogin(prefs.getPrefsLogin(), prefs.getPrefsPassword());
        viewModel.update(prefs.getPrefsId());
    }

    private void subscribe(GroupViewModel viewModel, StudentAttendancesAdapter attendancesAdapter, DatesAdapter datesAdapter) {
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state -> {
            if (state.getLoading()) {
                binding.tableContent.setVisibility(View.GONE);
                binding.tableErrorTv.setVisibility(View.GONE);
                binding.tableProgressBar.setVisibility(View.VISIBLE);
            } else {
                if (state.getSuccess()) {
                    binding.tableGroupName.setText(state.getGroupName());
                    binding.tableProgressBar.setVisibility(View.GONE);
                    binding.tableErrorTv.setVisibility(View.GONE);
                    binding.tableContent.setVisibility(View.VISIBLE);
                    if (state.getStudents().get(0).getAttendanceEntityList().isEmpty()) {
                        binding.buttonsAttPointsLayout.setVisibility(View.GONE);
                        binding.hsrStudentsTable.setVisibility(View.GONE);
                        binding.studentsRv.setVisibility(View.GONE);
                        binding.studentsEmptyLessonsRv.setVisibility(View.VISIBLE);

                        StudentsListAdapterForTable adapter = new StudentsListAdapterForTable();
                        binding.studentsEmptyLessonsRv.setAdapter(adapter);
                        adapter.updateData(state.getStudents());
                    } else {
                        binding.buttonsAttPointsLayout.setVisibility(View.VISIBLE);
                        binding.hsrStudentsTable.setVisibility(View.VISIBLE);
                        binding.studentsRv.setVisibility(View.VISIBLE);
                        binding.studentsEmptyLessonsRv.setVisibility(View.GONE);


                        attendancesAdapter.updateData(state.getStudents());
                        datesAdapter.update(viewModel.extractDates(state.getStudents().get(0).getAttendanceEntityList()));
                    }
                }
            }
        });

        viewModel.errorLiveData.observe(getViewLifecycleOwner(), errorMsg ->{
            binding.tableErrorTv.setVisibility(View.VISIBLE);
            binding.tableErrorTv.setText(errorMsg);
            binding.tableContent.setVisibility(View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            navigationBarChangeListener = (NavigationBarChangeListener) context;
            prefs = (UpdateSharedPreferences) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        viewModel.saveGroupIdByStudentId(prefs.getPrefsId());
//        Toast.makeText(requireContext(), "OnResume", Toast.LENGTH_SHORT).show();
    }
}
