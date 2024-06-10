package ru.technosopher.attendancelogappstudents.ui.scanner;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import ru.technosopher.attendancelogappstudents.R;
import ru.technosopher.attendancelogappstudents.databinding.FragmentScannerBinding;
import ru.technosopher.attendancelogappstudents.domain.entities.LessonEntity;
import ru.technosopher.attendancelogappstudents.ui.utils.NavigationBarChangeListener;
import ru.technosopher.attendancelogappstudents.ui.utils.UpdateSharedPreferences;
import ru.technosopher.attendancelogappstudents.ui.utils.DateFormatter;

public class ScannerFragment extends Fragment {

    public static final String SCANNER_GROUP_NAME = "GROUP_NAME";
    public static final String SCANNER_LESSON_DATE = "SCANNER_LESSON_DATE";
    public static final String SCANNER_LESSON_TIME = "LESSON_TIME";
    public static final String SCANNER_LESSON_THEME = "LESSON_THEME";
    private final String TAG = "SCANNER_FRAGMENT";

    private NavigationBarChangeListener navigationBarChangeListener;
    private UpdateSharedPreferences prefs;
    private ScannerViewModel viewModel;
    private FragmentScannerBinding binding;
    private View scannerView;
    private View scannerResultView;

    private DecoratedBarcodeView barcodeView;
    private boolean isScanning = true;

    private boolean isOverlayVisible;

    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean o) {
            if (o) {
                Log.d(TAG, "Permission granted! onActivityResult");
                initializeScanner();
            }
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scannerView = inflater.inflate(R.layout.fragment_scanner, container, false);
        return scannerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ScannerViewModel.class);
        binding = FragmentScannerBinding.bind(view);

        barcodeView = binding.barcodeScanner;

        binding.btnRefreshScan.setOnClickListener(v -> {
            binding.btnRefreshScan.setVisibility(View.GONE);
            isScanning = true;
        });

        if (allPermissionGranted()) {
            initializeScanner();
        } else {
            activityResultLauncher.launch(Manifest.permission.CAMERA);
        }
        subscribe(viewModel);
    }

    private void initializeScanner() {
        barcodeView.setStatusText("");
        barcodeView.resume();
        barcodeView.decodeContinuous(result -> {
            if (isScanning) {
                isScanning = false;
                viewModel.markAttended(result.getResult().getText(), prefs.getPrefsId());
            }
        });
    }

    private void subscribe(ScannerViewModel viewModel) {
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state -> {

            if (!state.getSuccess() && !state.getLoading()) {
                Toast.makeText(scannerView.getContext(), "Ошибка...", Toast.LENGTH_SHORT).show();
                binding.btnRefreshScan.setVisibility(View.VISIBLE);
                return;
            }

            LessonEntity lesson = state.getLesson();

            if (lesson == null) return;

            Bundle info = new Bundle();
            info.putString(SCANNER_GROUP_NAME, lesson.getGroupName());
            info.putString(SCANNER_LESSON_DATE, DateFormatter.getDateStringFromDate(lesson.getDate(), "dd.MM.YYYY"));
            info.putString(SCANNER_LESSON_TIME, DateFormatter.getFullTimeStringFromDate(lesson.getTimeStart(), lesson.getTimeEnd(), "HH:mm"));
            info.putString(SCANNER_LESSON_THEME, lesson.getTheme());

            BottomSheetLessonInfo bottomSheetLessonInfo = new BottomSheetLessonInfo();
            bottomSheetLessonInfo.setArguments(info);

            bottomSheetLessonInfo.show(requireActivity().getSupportFragmentManager(), TAG);

            binding.btnRefreshScan.setVisibility(View.VISIBLE);
        });
    }

    private boolean allPermissionGranted() {
        return ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
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
        Log.d(TAG, "Resuming scanner");
        if (barcodeView != null) {
            barcodeView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Pausing scanner");
        if (barcodeView != null) {
            barcodeView.pause();
        }
    }

    private void showInfo() {
        scannerResultView.setVisibility(View.VISIBLE);
        Animation slideIn = AnimationUtils.loadAnimation(scannerView.getContext(), R.anim.slide_in);
        scannerResultView.startAnimation(slideIn);

        isOverlayVisible = true;
    }

    private void hideInfo() {
        Animation slideOut = AnimationUtils.loadAnimation(scannerView.getContext(), R.anim.slide_out);
        slideOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                scannerResultView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        scannerResultView.startAnimation(slideOut);
        isOverlayVisible = false;
    }
}
