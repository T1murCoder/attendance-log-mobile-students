package ru.technosopher.attendancelogappstudents.ui.registration;

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

import ru.technosopher.attendancelogappstudents.R;
import ru.technosopher.attendancelogappstudents.ui.utils.OnChangeText;
import ru.technosopher.attendancelogappstudents.databinding.FragmentRegistrationBinding;
import ru.technosopher.attendancelogappstudents.ui.utils.Utils;
import ru.technosopher.attendancelogappstudents.ui.utils.NavigationBarChangeListener;
import ru.technosopher.attendancelogappstudents.ui.utils.UpdateSharedPreferences;


public class RegistrationFragment extends Fragment {

    private UpdateSharedPreferences prefs;
    private NavigationBarChangeListener navigationBarChangeListener;
    private FragmentRegistrationBinding binding;

    private RegistrationViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentRegistrationBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);

        try {
            navigationBarChangeListener.hideNavigationBar();
//            navigationBarChangeListener.changeSelectedItem(R.id.scannerFragment);
        } catch (ClassCastException ignored) {
        }

        binding.registrationConfirmButton.setOnClickListener(view1 -> {
            viewModel.confirm();
        });

        binding.registrationLoginEt.addTextChangedListener(new OnChangeText(){
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                viewModel.changeLogin(editable.toString());
            }
        });

        binding.registrationNameEt.addTextChangedListener(new OnChangeText(){
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                viewModel.changeName(editable.toString());
            }
        });

        binding.registrationSurnameEt.addTextChangedListener(new OnChangeText(){
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                viewModel.changeSurname(editable.toString());
            }
        });

        binding.registrationPasswordEt.addTextChangedListener(new OnChangeText(){
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                viewModel.changePassword(editable.toString());
            }
        });

        binding.registrationSignInLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_registrationFragment_to_loginFragment);
            }
        });
        binding.registrationRememberCb.setChecked(false);
        subscribe(viewModel);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            prefs = (UpdateSharedPreferences) context;
            navigationBarChangeListener = (NavigationBarChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    private void subscribe(RegistrationViewModel viewModel){
        viewModel.errorLiveData.observe(getViewLifecycleOwner(), error ->{
            binding.registrationAccountErrorTv.setVisibility(View.VISIBLE);
            binding.registrationAccountErrorTv.setText(error);
        });

        viewModel.userLiveData.observe(getViewLifecycleOwner(), state -> {

            prefs.updatePrefs(
                    state.getUser().getId(),
                    state.getUser().getUsername(),
                    state.getPassword(),
                    state.getUser().getName(),
                    state.getUser().getSurname(),
                    state.getUser().getTelegram_url(),
                    state.getUser().getGithub_url(),
                    state.getUser().getPhoto_url(),
                    binding.registrationRememberCb.isChecked()
            );
        });

        viewModel.confirmLiveData.observe(getViewLifecycleOwner(), unused -> {
            View view = getView();
            if (view == null) return;
            navigationBarChangeListener.showNavigationBar();
            Navigation.findNavController(view).navigate(R.id.action_registrationFragment_to_scannerFragment);
        });

        viewModel.loadingLiveData.observe(getViewLifecycleOwner(), loading ->{
            binding.loadingProgressBar.setVisibility(Utils.visibleOrGone(loading));
        });
    }

}
