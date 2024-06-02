package ru.technosopher.attendancelogappstudents.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import ru.technosopher.attendancelogappstudents.R;
import ru.technosopher.attendancelogappstudents.databinding.FragmentProfileBinding;
import ru.technosopher.attendancelogappstudents.domain.entities.UserEntity;
import ru.technosopher.attendancelogappstudents.ui.utils.NavigationBarChangeListener;
import ru.technosopher.attendancelogappstudents.ui.utils.UpdateSharedPreferences;

public class ProfileFragment extends Fragment {

    private NavigationBarChangeListener navigationBarChangeListener;
    private UpdateSharedPreferences prefs;
    private FragmentProfileBinding binding;

    private ProfileViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentProfileBinding.bind(view);
        navigationBarChangeListener.changeSelectedItem(R.id.profile);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        subscribe(viewModel);
        viewModel.loadPrefs(
                prefs.getPrefsId(),
                prefs.getPrefsLogin(),
                prefs.getPrefsName(),
                prefs.getPrefsSurname(),
                prefs.getPrefsTelegram(),
                prefs.getPrefsGithub(),
                prefs.getPrefsPhotoUrl()
        );

    }
    private void subscribe(ProfileViewModel viewModel){
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state -> {
            UserEntity user = state.getUser();

            if (user == null) return;

            binding.profileLoginTv.setText(user.getUsername());
            binding.profileNameEt.setText(user.getName());
            binding.profileSurnameEt.setText(user.getSurname());
            binding.profileTelegramEt.setText(user.getTelegram_url() != null ? user.getTelegram_url() : "Provide your telegram");
            binding.profileGithubEt.setText(user.getGithub_url() != null ? user.getGithub_url() : "Provide your github");
            // TODO (validate link)
            if (user.getPhoto_url() != null) Picasso.get().load(user.getPhoto_url()).into(binding.profileAvatarIv);

            //TODO (Do something with notifications)
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
}
