package ru.technosopher.attendancelogappstudents.ui.profile;

import static ru.technosopher.attendancelogappstudents.ui.MainActivity.FIREBASE_AVATAR_PREFIX;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileDescriptor;
import java.io.IOException;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ru.technosopher.attendancelogappstudents.data.repository.ImageRepositoryImpl;
import ru.technosopher.attendancelogappstudents.data.repository.UserRepositoryImpl;
import ru.technosopher.attendancelogappstudents.domain.entities.ImageEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.UserEntity;
import ru.technosopher.attendancelogappstudents.domain.images.UploadProfileImageUseCase;
import ru.technosopher.attendancelogappstudents.domain.sign.LogoutUseCase;
import ru.technosopher.attendancelogappstudents.domain.students.GetStudentProfileUseCase;
import ru.technosopher.attendancelogappstudents.domain.users.GetUserByIdUseCase;
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

    private final GetUserByIdUseCase getUserByIdUseCase = new GetUserByIdUseCase(
            UserRepositoryImpl.getInstance()
    );

    private final UpdateUserProfileUseCase updateUserProfileUseCase = new UpdateUserProfileUseCase(
            UserRepositoryImpl.getInstance()
    );

    private final UploadProfileImageUseCase uploadProfileImageUseCase = new UploadProfileImageUseCase(
            ImageRepositoryImpl.getINSTANCE()
    );

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    @Nullable
    private String id;
    @Nullable
    private String login;
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
        changeId(id);
        changeName(prefsName);
        changeSurname(prefsSurname);
        changeTelegram(prefsTelegram);
        changeGithub(prefsGithub);
        changePhoto(prefsPhotoUrl);
        changeLogin(prefsLogin);
    }

    public void update(String id, String prefsLogin, String prefsName, String prefsSurname, String prefsTelegram, String prefsGithub, String prefsPhotoUrl) {
        loadPrefs(id, prefsLogin, prefsName, prefsSurname, prefsTelegram, prefsGithub, prefsPhotoUrl);
        mutableStateLiveData.postValue(new State(null, new UserEntity(
                id,
                name,
                surname,
                login,
                telegram,
                github,
                photo
        ), false));
    }

    private void update(String photoUrl) {
        loadPrefs(id, login, name, surname, telegram, github, photoUrl);
        mutableStateLiveData.postValue(new State(null, new UserEntity(
                id,
                name,
                surname,
                login,
                telegram,
                github,
                photoUrl
        ), false));
    }

    @SuppressLint("CheckResult")
    public void uploadAvatar(String id, Uri image, ContentResolver contentResolver) {
        if (image != null) {
            mutableStateLiveData.postValue(new State("Загрузка...", null, false));
            try {

                Bitmap imageBitmap = getBitmapFromUri(image, contentResolver);
                if (imageBitmap != null) {
                    uploadProfileImageUseCase.execute(id, imageBitmap)
                            .subscribeOn(Schedulers.io())
                            .subscribe((photoUrl, throwable) -> {
                                if (throwable != null) {
                                    mutableStateLiveData.postValue(new State("Не удалось загрузить аватар!", null, false));
                                } else {
                                    Log.d(TAG, "Запрос сделан!");
                                    updateUserProfileUseCase.execute(
                                            id,
                                            new UserEntity(
                                                    id,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    photoUrl
                                            ),
                                            userStatus -> update(photoUrl));
                                }
                            });
                } else {
                    mutableStateLiveData.postValue(new State("Что-то пошло не так...", null, false));
                }
            } catch (IOException e) {
                mutableStateLiveData.postValue(new State("Что-то пошло не так...", null, false));
            }
        } else {
            mutableStateLiveData.postValue(new State("Пожалуйста, выберите изображение", null, false));
        }
    }

    private Bitmap getBitmapFromUri(Uri uri, ContentResolver contentResolver) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r");
        if (parcelFileDescriptor != null) {
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        }
        return null;
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
                        if (userstatus.getStatusCode() == 200) {
                            update(id, prefsLogin, name, surname, telegram, github, photo);
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
    public void changeLogin(String login) {this.login = login;}
    private void changeId(String id) {this.id = id;}

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
