package ru.technosopher.attendancelogappstudents.domain.images;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import ru.technosopher.attendancelogappstudents.domain.entities.ImageEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.Status;

public interface ImageRepository {

    void getProfileImage(@NonNull String imageUrl, Consumer<Status<ImageEntity>> callback);

    Single<String> uploadProfileImage(@NonNull String id, @NonNull Bitmap imageBitmap);
}
