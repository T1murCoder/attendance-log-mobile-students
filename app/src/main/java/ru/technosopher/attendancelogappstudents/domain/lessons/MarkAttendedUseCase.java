package ru.technosopher.attendancelogappstudents.domain.lessons;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.domain.entities.LessonEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.Status;

public class MarkAttendedUseCase {

    private final LessonRepository repository;

    public MarkAttendedUseCase(LessonRepository lessonRepository) {
        this.repository = lessonRepository;
    }

    public void execute(@NonNull String qrCodeId,
                        @NonNull String studentId,
                        @NonNull Consumer<Status<LessonEntity>> callback) {
//        repository.markAttendedByLessonId(lessonId, studentId, callback);
        repository.markAttendedByQRCodeId(qrCodeId, studentId, callback);
    }
}
