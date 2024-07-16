package ru.technosopher.attendancelogappstudents.ui.profile;

import static ru.technosopher.attendancelogappstudents.ui.MainActivity.FIREBASE_AVATAR_PREFIX;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import ru.technosopher.attendancelogappstudents.data.repository.UserRepositoryImpl;
import ru.technosopher.attendancelogappstudents.domain.entities.UserEntity;
import ru.technosopher.attendancelogappstudents.domain.sign.LogoutUseCase;
import ru.technosopher.attendancelogappstudents.domain.users.UpdateUserProfileUseCase;

public class ProfileViewModel extends ViewModel {

    public static final String TAG = "PROFILE_VIEW_MODEL";

    private final MutableLiveData<State> mutableStateLiveData = new MutableLiveData<>();
    public final LiveData<State> stateLiveData = mutableStateLiveData;

    private final MutableLiveData<Void> mutableLogoutLiveData = new MutableLiveData<>();
    public final LiveData<Void> logoutLiveData = mutableLogoutLiveData;

    private final LogoutUseCase logoutUseCase = new LogoutUseCase(
            UserRepositoryImpl.getInstance()
    );

    private final UpdateUserProfileUseCase updateUserProfileUseCase = new UpdateUserProfileUseCase(
            UserRepositoryImpl.getInstance()
    );

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

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
        changePhoto(prefsPhotoUrl);
    }

    public void uploadAvatar(String id, String prefsLogin, Uri image) {
        if (image != null) {
            StorageReference imageRef = storageRef.child(FIREBASE_AVATAR_PREFIX + id + ".png");

            imageRef.putFile(image).addOnSuccessListener(taskSnapshot -> {
                Log.d(TAG, "Image loaded!");
                updateUserProfileUseCase.execute(
                        id,
                        new UserEntity(
                                id,
                                null,
                                null,
                                null,
                                null,
                                null,
                                imageRef.getPath()
                        ),
                        userStatus -> loadPrefs(id, prefsLogin, name, surname, telegram, github, imageRef.getPath())
                );
            }).addOnFailureListener(e -> {
                Log.d(TAG, e.toString());
                mutableStateLiveData.postValue(new State("Не получилось загрузить аватар!", null, false));
            });
        } else {
            Log.d(TAG, "Image is null!");
        }
    }

    public void updateProfile(String id, String prefsLogin) {
        if (name == null || surname == null || name.isEmpty() || surname.isEmpty()) {
            mutableStateLiveData.postValue(new State("Имя и фамилия не могут быть пустыми", null, false));
        } else {
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
//                        System.out.println(userstatus.getStatusCode());
//                        System.out.println(userstatus.getValue());
                        if (userstatus.getStatusCode() == 200) {
                            loadPrefs(id, prefsLogin, name, surname, telegram, github, photo);
                        } else {
                            mutableStateLiveData.postValue(new State("Что то пошло не так. Попробуйте еще раз", null, false));
                        }

                    });
        }
    }

    public void changeName(String name){
        this.name = name.trim();
    }
    public void changeSurname(String surname){
        this.surname = surname.trim();
    }
    public void changeTelegram(String telegram){
        this.telegram = telegram;
    }
    public void changeGithub(String github){
        this.github = github;
    }
    public void changePhoto(String photo) {this.photo = photo;}

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
