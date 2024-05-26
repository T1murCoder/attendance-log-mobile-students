package ru.technosopher.attendancelogappstudents.ui.scanner;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import ru.technosopher.attendancelogappstudents.R;
import ru.technosopher.attendancelogappstudents.databinding.FragmentScannerBinding;
import ru.technosopher.attendancelogappstudents.domain.entities.LessonEntity;
import ru.technosopher.attendancelogappstudents.ui.NavigationBarChangeListener;
import ru.technosopher.attendancelogappstudents.ui.UpdateSharedPreferences;
import ru.technosopher.attendancelogappstudents.ui.utils.DateFormatter;

public class ScannerFragment extends Fragment {

    private final String TAG = "SCANNER_FRAGMENT";

    private NavigationBarChangeListener navigationBarChangeListener;
    private UpdateSharedPreferences prefs;
    private ScannerViewModel viewModel;
    private FragmentScannerBinding binding;
    private View scannerView;
    private View scannerResultView;

    private DecoratedBarcodeView barcodeView;
    private boolean isScanning = true;

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
        scannerResultView = scannerView.findViewById(R.id.scanned_group_result);
        return scannerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ScannerViewModel.class);
        binding = FragmentScannerBinding.bind(view);

        barcodeView = binding.barcodeScanner;

        ImageButton closeGroupInfoButton = scannerView.findViewById(R.id.btn_close_group_info);

        closeGroupInfoButton.setOnClickListener(v -> {
            scannerResultView.setVisibility(View.GONE);
            binding.btnRefreshScan.setVisibility(View.VISIBLE);
        });

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
                return;
            }

            LessonEntity lesson = state.getLesson();

            if (lesson == null) return;

            binding.scannedGroupName.setText(lesson.getGroupName());
            binding.scannedGroupDate.setText(DateFormatter.getDateStringFromDate(lesson.getDate(), "dd.MM.YYYY"));
            String time = DateFormatter.getFullTimeStringFromDate(lesson.getTimeStart(), lesson.getTimeEnd(), "HH:mm");
            binding.scannedGroupTime.setText(time);
            binding.scannedGroupTheme.setText(lesson.getTheme());

            scannerResultView.setVisibility(View.VISIBLE);
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
}
