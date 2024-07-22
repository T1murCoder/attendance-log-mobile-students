package ru.technosopher.attendancelogappstudents.domain.images;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class UploadProfileImageUseCase {

    private final ImageRepository repository;

    public UploadProfileImageUseCase(ImageRepository repository) {
        this.repository = repository;
    }

    public Single<String> execute(@NonNull String id, @NonNull Bitmap imageBitmap) {
        return repository.uploadProfileImage(id, imageBitmap);
    }
}
