package ru.technosopher.attendancelogappstudents.ui.studentprofile;

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
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.databinding.FragmentStudentProfileBinding;
import ru.technosopher.attendancelogappstudents.domain.entities.UserEntity;
import ru.technosopher.attendancelogappstudents.ui.utils.NavigationBarChangeListener;

import ru.technosopher.attendancelogappstudents.R;

public class StudentProfileFragment extends Fragment {
    public static final String TAG = "STUDENT_PROFILE_FRAGMENT";
    private static final String KEY_ID = "STUDENT_PROFILE_FRAGMENT";
    private NavigationBarChangeListener navigationBarChangeListener;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private StudentProfileViewModel viewModel;
    private FragmentStudentProfileBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        navigationBarChangeListener.hideNavigationBar();
        binding = FragmentStudentProfileBinding.bind(view);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == null) return;
                Navigation.findNavController(view).navigate(R.id.action_studentProfileFragment_to_groupFragment);
            }
        });

        binding.swipe.setOnRefreshListener(() -> {
            viewModel.update();
        });


        String id = getArguments() != null ? getArguments().getString(KEY_ID) : "-1";
        viewModel = new ViewModelProvider(this).get(StudentProfileViewModel.class);
        viewModel.saveStudentId(id);

        viewModel.update();
        subscribe(viewModel);
    }

    private void subscribe(StudentProfileViewModel viewModel) {
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state -> {
            if(Boolean.TRUE.equals(state.getLoading())) {
                binding.profileLoading.setVisibility(View.VISIBLE);
                binding.profileContent.setVisibility(View.GONE);
                binding.swipe.setEnabled(false);
                binding.swipe.setVisibility(View.GONE);
            } else {
                binding.profileLoading.setVisibility(View.GONE);
                binding.profileContent.setVisibility(View.VISIBLE);
                binding.swipe.setEnabled(true);
                binding.swipe.setRefreshing(false);
                binding.swipe.setVisibility(View.VISIBLE);
                UserEntity student = state.getStudent();
                Log.d(TAG, String.valueOf(student));
                if (student != null) {
                    binding.profileLoginTv.setText(student.getUsername());
                    binding.profileNameEt.setText(student.getName());
                    binding.profileSurnameEt.setText(student.getSurname());

                    if (student.getTelegram_url() != null) {
                        binding.profileTelegramEt.setText(student.getTelegram_url());
                        binding.telegramContainerStudentProfile.setOnLongClickListener(v -> {
                            setClipboard(student.getTelegram_url());
                            return true;
                        });
                    } else {
                        binding.profileTelegramEt.setText("...");
                    }
                    if (student.getGithub_url() != null) {
                        binding.profileGithubEt.setText(student.getGithub_url());
                        binding.githubContainerStudentProfile.setOnLongClickListener(v -> {
                            setClipboard(student.getGithub_url());
                            return true;
                        });
                    } else {
                        binding.profileGithubEt.setText("...");
                    }

                    if (student.getPhoto_url() != null) {
                        loadAvatar(student.getPhoto_url());
                    }
                } else {
                    Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadAvatar(String imageUrl) {
        StorageReference imageRef = storageRef.child(imageUrl);

        imageRef.getDownloadUrl().addOnCompleteListener(task -> {
            if (task != null && task.getResult() != null && task.isSuccessful()) {
                Glide.with(requireContext()).load(task.getResult()).into(binding.profileAvatarIv);
            }
            Log.d(TAG, "loadAvatar: " + task.isSuccessful());
        }).addOnFailureListener(e -> {
            Log.d(TAG, "loadAvatar: " + false);
        });
    }

    private void setClipboard(String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(requireContext(), "Скопировано!", Toast.LENGTH_SHORT).show();
    }

    public static Bundle getBundle(@NonNull String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID, id);
        return bundle;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            navigationBarChangeListener = (NavigationBarChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

}
