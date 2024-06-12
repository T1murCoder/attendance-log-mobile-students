package ru.technosopher.attendancelogappstudents.domain.lessons;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.domain.entities.LessonEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.Status;

public interface LessonRepository {
    void getLessonById(@NonNull String id, @NonNull Consumer<Status<LessonEntity>> callback);

    void markAttendedByLessonId(@NonNull String lessonId, @NonNull String studentId, @NonNull Consumer<Status<LessonEntity>> callback);

    void markAttendedByQRCodeId(@NonNull String qrCodeId, @NonNull String studentId, @NonNull Consumer<Status<LessonEntity>> callback);

}
