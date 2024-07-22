package ru.technosopher.attendancelogappstudents.ui.group;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import ru.technosopher.attendancelogappstudents.R;
import ru.technosopher.attendancelogappstudents.data.source.CredentialsDataSource;
import ru.technosopher.attendancelogappstudents.databinding.FragmentGroupBinding;
import ru.technosopher.attendancelogappstudents.ui.studentprofile.StudentProfileFragment;
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

        Log.d(TAG, prefs.getPrefsId());


        viewModel = new ViewModelProvider(this).get(GroupViewModel.class);

        StudentAttendancesAdapter attendancesAdapter = new StudentAttendancesAdapter(getContext(), true, this::openStudentProfile);
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
//        String[] months = getResources().getStringArray(R.array.group_dropdown_months);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_spinner, months);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        binding.dateHeaderSpinner.setAdapter(arrayAdapter);
//        binding.dateHeaderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                viewModel.filterGroupByMonth(getCalendarByMonth(months[position]));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                viewModel.filterGroupByMonth(null);
//            }
//        });

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

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
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

                        StudentsListAdapterForTable adapter = new StudentsListAdapterForTable(this::openStudentProfile);
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

        viewModel.errorLiveData.observe(getViewLifecycleOwner(), errorMsg -> {
            binding.tableErrorTv.setVisibility(View.VISIBLE);
            binding.tableErrorTv.setText(errorMsg);
            binding.tableContent.setVisibility(View.GONE);
        });
    }

    private GregorianCalendar getCalendarByMonth(String month) {

        String monthLower = month.toLowerCase().trim();

        String[] months = getResources().getStringArray(R.array.months_strings);

        int monthIndex = -1;

        for (int i = 0; i < months.length; i++) {
            if (months[i].toLowerCase().equals(monthLower)) {
                monthIndex = i;
                break;
            }
        }

        if (monthIndex == -1) {
            return null;
        }

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.set(Calendar.MONTH, monthIndex);

        return calendar;
    }

    private void openStudentProfile(@NonNull String id) {
        View view = getView();
        if (view == null) return;
        Navigation.findNavController(view).navigate(R.id.action_groupFragment_to_studentProfileFragment, StudentProfileFragment.getBundle(id));
    }
}
