package ru.technosopher.attendancelogappstudents.data.source;

import static ru.technosopher.attendancelogappstudents.ui.MainActivity.FIREBASE_AVATAR_PREFIX;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.reactivex.rxjava3.core.Completable;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.http.HTTP;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import ru.technosopher.attendancelogappstudents.data.utils.CallToConsumer;

public class FirebaseImageSource {
    public static final String FIREBASE_AVATAR_PREFIX = "images/avatar_";

    private static FirebaseImageSource INSTANCE;

    public final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    public final StorageReference storageRef = firebaseStorage.getReference();


    public static synchronized FirebaseImageSource getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new FirebaseImageSource();
        }
        return INSTANCE;
    }

    // TODO: Implement this class

    private FirebaseImageSource() {}

//    public Uri getImage(String imageUrl) {
//        StorageReference imageRef = storageRef.child(imageUrl);
//
//    }


//    public String uploadImage(@NonNull String id, @NonNull Uri image) {
//
////        StorageReference imageRef = storageRef.child(FIREBASE_AVATAR_PREFIX + id + ".png");
////
////        UploadTask uploadTask = imageRef.putFile(image);
////        uploadTask.addOnSuccessListener(
////
////        ).addOnFailureListener(
////
////        );
//    }
}
