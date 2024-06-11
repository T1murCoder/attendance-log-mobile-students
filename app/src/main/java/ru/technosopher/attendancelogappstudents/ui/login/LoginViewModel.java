package ru.technosopher.attendancelogappstudents.ui.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.technosopher.attendancelogappstudents.data.UserRepositoryImpl;
import ru.technosopher.attendancelogappstudents.data.source.CredentialsDataSource;
import ru.technosopher.attendancelogappstudents.domain.entities.UserEntity;
import ru.technosopher.attendancelogappstudents.domain.sign.IsUserExistsUseCase;
import ru.technosopher.attendancelogappstudents.domain.sign.LoginUserUseCase;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<Void> mutableConfirmLiveData = new MutableLiveData<>();
    public final LiveData<Void> confirmLiveData = mutableConfirmLiveData;
    private final MutableLiveData<String> mutableErrorLiveData = new MutableLiveData<>();
    public final LiveData<String> errorLiveData = mutableErrorLiveData;
    private final MutableLiveData<State> mutableUserLiveData = new MutableLiveData<>();
    public final LiveData<State> userLiveData = mutableUserLiveData;

    private final MutableLiveData<Boolean> mutableLoadingLiveData = new MutableLiveData<>();
    public final LiveData<Boolean> loadingLiveData = mutableLoadingLiveData;

    @Nullable
    private String login = null;
    @Nullable
    private String password = null;


    /*  USE CASES  */
    IsUserExistsUseCase isTeacherExistsUseCase = new IsUserExistsUseCase(
            UserRepositoryImpl.getInstance()
    );
    LoginUserUseCase loginUserUseCase = new LoginUserUseCase(
            UserRepositoryImpl.getInstance()
    );
    /*  USE CASES  */


    public void changeLogin(@NonNull String login) {
        this.login = login;
    }

    public void changePassword(@NonNull String password) {
        this.password = password;
    }

    public void confirm() {
        CredentialsDataSource.getInstance().logout();
        final String currentLogin = login;
        final String currentPassword = password;

        if ((currentLogin == null || currentLogin.isEmpty()) && (currentPassword == null || currentPassword.isEmpty())) {
            mutableErrorLiveData.postValue("Please, enter login and password.");
            return;
        }
        if (currentLogin == null || currentLogin.isEmpty()) {
            mutableErrorLiveData.postValue("Please, enter login.");
            return;
        }
        if (currentPassword == null || currentPassword.isEmpty()) {
            mutableErrorLiveData.postValue("Please, enter password.");
            return;
        }
        mutableLoadingLiveData.postValue(true);

        isTeacherExistsUseCase.execute(currentLogin, status -> {
            if (status.getErrors() != null || status.getValue() == null) {
//                System.out.println(status.getErrors().getLocalizedMessage());
                mutableLoadingLiveData.postValue(false);
                mutableErrorLiveData.postValue("Something went wrong with server. Try again later");
                Log.d("LOGIN_VIEWMODEL", "" + status.getStatusCode());
                return;
            }
            if (status.getStatusCode() == 404) {
                mutableLoadingLiveData.postValue(false);
                mutableErrorLiveData.postValue("This login doesn`t exist. Want to create account?");
                return;
            }
            if (status.getStatusCode() == 200) {
                loginUser(currentLogin, currentPassword);
            }
        });
    }

    private void loginUser(@NonNull final String currentLogin, @NonNull final String currentPassword) {
        loginUserUseCase.execute(currentLogin, currentPassword, status -> {
            if (status.getErrors() == null && status.getStatusCode() == 200) {

                login = currentLogin;
                password = currentPassword;



                mutableUserLiveData.postValue(new State(new UserEntity(
                        status.getValue().getId(),
                        status.getValue().getName(),
                        status.getValue().getSurname(),
                        status.getValue().getUsername(),
                        status.getValue().getTelegram_url(),
                        status.getValue().getGithub_url(),
                        status.getValue().getPhoto_url()

                ), password));

                mutableConfirmLiveData.postValue(null);
            }
            if (status.getErrors() == null && status.getStatusCode() == 401) {
                mutableErrorLiveData.postValue("Wrong password");
            }
            mutableLoadingLiveData.postValue(false);
        });
    }

    public class State {

        @Nullable
        private final UserEntity user;
        @Nullable
        private final String password;

        public State(@Nullable UserEntity user, @Nullable String password) {
            this.user = user;
            this.password = password;
        }
        @Nullable
        public String getPassword() {
            return password;
        }
        @Nullable
        public UserEntity getUser() {
            return user;
        }
    }
}
