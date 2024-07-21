package ru.technosopher.attendancelogappstudents.data.repository;

import static ru.technosopher.attendancelogappstudents.data.utils.ImageCompressor.convertBitmapToBytes;
import static ru.technosopher.attendancelogappstudents.data.utils.ImageCompressor.resizeBitmap;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.function.Consumer;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import ru.technosopher.attendancelogappstudents.data.source.FirebaseImageSource;
import ru.technosopher.attendancelogappstudents.data.utils.CallToConsumer;
import ru.technosopher.attendancelogappstudents.domain.entities.ImageEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.Status;
import ru.technosopher.attendancelogappstudents.domain.images.ImageRepository;

public class ImageRepositoryImpl implements ImageRepository {

    public static final String FIREBASE_AVATAR_PREFIX = "images/avatar_";
    public static final String TAG = "IMAGE_REPOSITORY_IMPL";


    private static ImageRepositoryImpl INSTANCE;

    private FirebaseImageSource fbService = FirebaseImageSource.getINSTANCE();

    public static ImageRepositoryImpl getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ImageRepositoryImpl();
        }
        return INSTANCE;
    }

    private ImageRepositoryImpl() {}

    @Override
    public void getProfileImage(@NonNull String imageUrl, Consumer<Status<ImageEntity>> callback) {
        // TODO: Implement this?
    }

    @Override
    public Single<String> uploadProfileImage(@NonNull String id, @NonNull Bitmap imageBitmap) {
        return Single.create(emitter -> {
            StorageReference imageRef = fbService.storageRef.child(FIREBASE_AVATAR_PREFIX + id + ".png");

            Bitmap resizedImage = resizeBitmap(imageBitmap, 256, 256);
            byte[] resizedImageBytes = convertBitmapToBytes(resizedImage, Bitmap.CompressFormat.PNG, 100);

            UploadTask uploadTask = imageRef.putBytes(resizedImageBytes);
            uploadTask.addOnCompleteListener(command -> {
                    Log.d(TAG, "Image successfully uploaded!");
                    emitter.onSuccess(imageRef.getPath());
        })
                    .addOnFailureListener(e -> {
                        emitter.onError(e);
                        Log.d(TAG, e.toString());
                    });
        });
    }

}
