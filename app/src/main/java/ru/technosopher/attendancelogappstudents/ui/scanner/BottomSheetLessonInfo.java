package ru.technosopher.attendancelogappstudents.ui.scanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ru.technosopher.attendancelogappstudents.R;
import ru.technosopher.attendancelogappstudents.databinding.DialogBottomSheetLessonInfoBinding;

public class BottomSheetLessonInfo extends BottomSheetDialogFragment {
    private DialogBottomSheetLessonInfoBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DialogBottomSheetLessonInfoBinding.bind(inflater.inflate(R.layout.dialog_bottom_sheet_lesson_info, container, false));

        final Bundle info = getArguments();

        String groupName = info.getString(ScannerFragment.SCANNER_GROUP_NAME);
        String lessonDate = info.getString(ScannerFragment.SCANNER_LESSON_DATE);
        String lessonTime = info.getString(ScannerFragment.SCANNER_LESSON_TIME);
        String lessonTheme = info.getString(ScannerFragment.SCANNER_LESSON_THEME);

        if (groupName != null && lessonDate != null && lessonTime != null && lessonTheme != null) {
            binding.scannedGroupName.setText(groupName);
            binding.scannedLessonDate.setText(lessonDate);
            binding.scannedLessonTime.setText(lessonTime);
            binding.scannedLessonTheme.setText(lessonTheme);

        } else {
            // Ничего не делаем, аргументы всегда не null
        }


        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
