package ru.technosopher.attendancelogappstudents.ui.scanner;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.technosopher.attendancelogappstudents.data.repository.LessonRepositoryImpl;
import ru.technosopher.attendancelogappstudents.domain.entities.LessonEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.Status;
import ru.technosopher.attendancelogappstudents.domain.lessons.MarkAttendedUseCase;

public class ScannerViewModel extends ViewModel {
    public static final String TAG = "ScannerViewModel";

    private final MutableLiveData<LessonState> mutableLessonLiveData = new MutableLiveData<>();
    public final LiveData<LessonState> stateLiveData = mutableLessonLiveData;

    private final MarkAttendedUseCase addAttendanceUseCase = new MarkAttendedUseCase(
            LessonRepositoryImpl.getINSTANCE()
    );

    @Nullable
    private LessonEntity lesson = null;

    public void update() {
        mutableLessonLiveData.postValue(new LessonState(null, null, false, true));
    }

    public void markAttended(@NonNull String qrCodeId, @NonNull String studentId) {
        addAttendanceUseCase.execute(qrCodeId, studentId, status -> {
            if (status.getStatusCode() == 200 && status.getErrors() == null) {
                if (status.getValue() != null) {
                    mutableLessonLiveData.postValue(fromLessonStatus(status));
                }
            } else {
                // TODO: Add Error
                mutableLessonLiveData.postValue(fromLessonStatus(status));
                Log.d(TAG, "Something going wrong");
                Log.d(TAG, "" + status.getStatusCode());
//                Log.d(TAG, status.getErrors().getMessage());
                clearAllFields();
            }
        });
    }

    public void clearAllFields() {
        lesson = null;
    }

    private LessonState fromLessonStatus(Status<LessonEntity> status) {
        if (status.getErrors() == null && status.getValue() != null) {
            lesson = status.getValue();
        }
        return new LessonState(
                status.getValue() != null ? status.getValue() : null,
                status.getErrors() == null ? null : status.getErrors().getLocalizedMessage(),
                status.getErrors() == null && status.getValue() != null,
                false
        );
    }

    public class LessonState {
        @Nullable
        private final LessonEntity lessonEntity;

        @Nullable
        private final String errorMessage;

        @NonNull
        private final Boolean isSuccess;

        @NonNull
        private final Boolean isLoading;

        public LessonState(@Nullable LessonEntity lessonEntity, @Nullable String errorMessage, @NonNull Boolean isSuccess, @NonNull Boolean isLoading) {
            this.lessonEntity = lessonEntity;
            this.errorMessage = errorMessage;
            this.isSuccess = isSuccess;
            this.isLoading = isLoading;
        }

        @Nullable
        public LessonEntity getLesson() {
            return lessonEntity;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @NonNull
        public Boolean getSuccess() {
            return isSuccess;
        }

        @NonNull
        public Boolean getLoading() {
            return isLoading;
        }
    }
}
