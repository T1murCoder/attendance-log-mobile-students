package ru.technosopher.attendancelogappstudents.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.squareup.picasso.Picasso;

import ru.technosopher.attendancelogappstudents.R;
import ru.technosopher.attendancelogappstudents.databinding.FragmentProfileBinding;
import ru.technosopher.attendancelogappstudents.domain.entities.UserEntity;
import ru.technosopher.attendancelogappstudents.ui.utils.NavigationBarChangeListener;
import ru.technosopher.attendancelogappstudents.ui.utils.OnChangeText;
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
//        navigationBarChangeListener.changeSelectedItem(R.id.profile);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding.profileLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.logout();
            }
        });

        binding.profileNameEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.profileNameEt.isFocusable()){
                    binding.profileNameEt.setFocusable(true);
                    binding.profileNameEt.setFocusableInTouchMode(true);
                    binding.profileNameEt.setEnabled(true);
                }else{
                    binding.profileNameEt.setFocusable(false);
                    binding.profileNameEt.setFocusableInTouchMode(false);
                    binding.profileNameEt.setEnabled(false);
                }

            }
        });
        binding.profileSurnameEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.profileSurnameEt.isFocusable()){
                    binding.profileSurnameEt.setFocusable(true);
                    binding.profileSurnameEt.setFocusableInTouchMode(true);
                    binding.profileSurnameEt.setEnabled(true);
                }else{
                    binding.profileSurnameEt.setFocusable(false);
                    binding.profileSurnameEt.setFocusableInTouchMode(false);
                    binding.profileSurnameEt.setEnabled(false);
                }
            }
        });
        binding.profileTelegramEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.profileTelegramEt.isFocusable()){
                    binding.profileTelegramEt.setFocusable(true);
                    binding.profileTelegramEt.setFocusableInTouchMode(true);
                    binding.profileTelegramEt.setEnabled(true);
                }else{
                    binding.profileTelegramEt.setFocusable(false);
                    binding.profileTelegramEt.setFocusableInTouchMode(false);
                    binding.profileTelegramEt.setEnabled(false);
                }
            }
        });
        binding.profileGithubEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.profileGithubEt.isFocusable()){
                    binding.profileGithubEt.setFocusable(true);
                    binding.profileGithubEt.setFocusableInTouchMode(true);
                    binding.profileGithubEt.setEnabled(true);
                }else{
                    binding.profileGithubEt.setFocusable(false);
                    binding.profileGithubEt.setFocusableInTouchMode(false);
                    binding.profileGithubEt.setEnabled(false);
                }
            }
        });

        binding.profileNameEt.addTextChangedListener(new OnChangeText(){
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                viewModel.changeName(editable.toString());
            }
        });
        binding.profileSurnameEt.addTextChangedListener(new OnChangeText(){
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                viewModel.changeSurname(editable.toString());
            }
        });

        binding.profileTelegramEt.addTextChangedListener(new OnChangeText(){
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                viewModel.changeTelegram(editable.toString());
            }
        });
        binding.profileGithubEt.addTextChangedListener(new OnChangeText(){
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                viewModel.changeGithub(editable.toString());
            }
        });

        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.updateProfile(prefs.getPrefsId(), prefs.getPrefsLogin());
            }
        });

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

            viewModel.logoutLiveData.observe(getViewLifecycleOwner(), unused -> {
                prefs.clearAll();
                View view = getView();
                if (view == null) return;
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_loginFragment);
//            Navigation.findNavController(view).navigate(
//                    R.id.action_profileFragment_to_loginFragment,
//                    null,
//                    new NavOptions.Builder().setPopUpTo(R.id.profileFragment, true).build());
            });
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
