package ru.technosopher.attendancelogappstudents.ui.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.technosopher.attendancelogappstudents.data.UserRepositoryImpl;
import ru.technosopher.attendancelogappstudents.domain.GetUserByIdUseCase;
import ru.technosopher.attendancelogappstudents.domain.entities.UserEntity;
import ru.technosopher.attendancelogappstudents.domain.sign.LogoutUseCase;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<State> mutableStateLiveData = new MutableLiveData<>();
    public final LiveData<State> stateLiveData = mutableStateLiveData;

    private final MutableLiveData<Void> mutableLogoutLiveData = new MutableLiveData<>();
    public final LiveData<Void> logoutLiveData = mutableLogoutLiveData;

    public final GetUserByIdUseCase getTeacherByIdUseCase = new GetUserByIdUseCase(
            UserRepositoryImpl.getInstance()
    );

    private final LogoutUseCase logoutUseCase = new LogoutUseCase(
            UserRepositoryImpl.getInstance()
    );

    public void load(@NonNull String id) {
        getTeacherByIdUseCase.execute(id, status -> {
            mutableStateLiveData.postValue(new State(
                    status.getErrors() != null ? status.getErrors().getLocalizedMessage() : null,
                    status.getValue()
            ));
        });
    }

    public void updatePrefs() {
        //TODO(SUFFER.............)
    }

    public void loadPrefs(String id, String prefsLogin, String prefsName, String prefsSurname, String prefsTelegram, String prefsGithub, String prefsPhotoUrl) {
        // TODO(VALIDATION)
        mutableStateLiveData.postValue(new State(null, new UserEntity(
                id,
                prefsName,
                prefsSurname,
                prefsLogin,
                prefsTelegram,
                prefsGithub,
                prefsPhotoUrl
        )));
    }

    public void logout() {
        logoutUseCase.execute();
        mutableLogoutLiveData.postValue(null);
    }


    public class State {
        @Nullable
        private final String errorMessage;
        @Nullable
        private final UserEntity user;

        public State(@Nullable String errorMessage, @Nullable UserEntity user) {
            this.errorMessage = errorMessage;
            this.user = user;
        }

        @Nullable
        public UserEntity getUser() {
            return user;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

    }
}
