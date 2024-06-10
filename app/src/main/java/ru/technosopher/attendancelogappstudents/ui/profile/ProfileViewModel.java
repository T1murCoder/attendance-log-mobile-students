package ru.technosopher.attendancelogappstudents.ui.profile;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.technosopher.attendancelogappstudents.data.UserRepositoryImpl;
import ru.technosopher.attendancelogappstudents.domain.users.GetUserByIdUseCase;
import ru.technosopher.attendancelogappstudents.domain.entities.UserEntity;
import ru.technosopher.attendancelogappstudents.domain.sign.LogoutUseCase;
import ru.technosopher.attendancelogappstudents.domain.users.UpdateUserProfileUseCase;

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

    private final UpdateUserProfileUseCase updateUserProfileUseCase = new UpdateUserProfileUseCase(
            UserRepositoryImpl.getInstance()
    );

    @Nullable
    private String name;

    @Nullable
    private String surname;

    @Nullable
    private String telegram;

    @Nullable
    private String github;

    @Nullable
    private String photo;

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
        ), false));
        changeName(prefsName);
        changeSurname(prefsSurname);
        changeTelegram(prefsTelegram);
        changeGithub(prefsGithub);
    }

    public void updateProfile(String id, String prefsLogin) {
        if (name == null || surname == null || name.isEmpty() || surname.isEmpty()) {
            mutableStateLiveData.postValue(new State("Имя и фамилия не могут быть пустыми", null, false));
        } else {
            //TODO(fix untouched fields update)
            mutableStateLiveData.postValue(new State(null, null, true));
            updateUserProfileUseCase.execute(
                    id,
                    new UserEntity(
                            id,
                            name,
                            surname,
                            prefsLogin,
                            telegram,
                            github,
                            photo),
                    userstatus -> {
                        System.out.println(userstatus.getStatusCode());
                        System.out.println(userstatus.getValue());
                        if (userstatus.getStatusCode() == 200) {
                            loadPrefs(id, prefsLogin, name, surname, telegram, prefsLogin, photo);
                        } else {
                            mutableStateLiveData.postValue(new State("Что то пошло не так. Попробуйте еще раз", null, false));
                        }

                    });
        }
    }

    public void changeName(String name){
        this.name = name;
    }
    public void changeSurname(String surname){
        this.surname = surname;
    }
    public void changeTelegram(String telegram){
        this.telegram = telegram;
    }
    public void changeGithub(String github){
        this.github = github;
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

        @Nullable
        private final Boolean loading;

        public State(@Nullable String errorMessage, @Nullable UserEntity user, @Nullable Boolean loading) {
            this.errorMessage = errorMessage;
            this.user = user;
            this.loading = loading;
        }

        @Nullable
        public UserEntity getUser() {
            return user;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @Nullable
        public Boolean getLoading() {
            return loading;
        }
    }
}
