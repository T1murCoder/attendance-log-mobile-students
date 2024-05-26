package ru.technosopher.attendancelogappstudents.ui.scanner;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.util.concurrent.ListenableFuture;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import ru.technosopher.attendancelogappstudents.R;
import ru.technosopher.attendancelogappstudents.databinding.FragmentScannerBinding;
import ru.technosopher.attendancelogappstudents.domain.entities.LessonEntity;
import ru.technosopher.attendancelogappstudents.ui.NavigationBarChangeListener;
import ru.technosopher.attendancelogappstudents.ui.UpdateSharedPreferences;
import ru.technosopher.attendancelogappstudents.ui.login.LoginViewModel;
import ru.technosopher.attendancelogappstudents.ui.profile.ProfileViewModel;
import ru.technosopher.attendancelogappstudents.ui.utils.DateFormatter;

public class ScannerFragment extends Fragment {

    private NavigationBarChangeListener navigationBarChangeListener;
    private UpdateSharedPreferences prefs;
    private ScannerViewModel viewModel;
    private FragmentScannerBinding binding;
    private View scannerView;
    private View scannerResultView;

    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean o) {
            if (o) {
                Log.d("SCANNER_FRAGMENT", "Permission granted! onActivityResult");
                scanCode();
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

        Button scanQrButton = scannerView.findViewById(R.id.btn_scan_qr);

        scanQrButton.setOnClickListener(v -> {
            if (allPermissionGranted()) {
                scanCode();
            } else {
                activityResultLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        ImageButton closeGroupInfoButton = scannerView.findViewById(R.id.btn_close_group_info);

        closeGroupInfoButton.setOnClickListener(v -> {
            scannerResultView.setVisibility(View.GONE);
        });
        subscribe(viewModel);
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

    private void scanCode() {
        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setPrompt("");
        scanOptions.setTorchEnabled(false);
        scanOptions.setOrientationLocked(true);
        scanOptions.setBeepEnabled(false);
        scanOptions.setCaptureActivity(QRCodeCaptureActivity.class);
        barLauncher.launch(scanOptions);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result ->
    {
       if(result.getContents() != null) {

//           viewModel

           //FIXME: получать айди текущего пользователя
           viewModel.markAttended(result.getContents(), prefs.getPrefsId());

       }
    });

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
}
