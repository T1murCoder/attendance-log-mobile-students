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

    public void execute(@NonNull String lessonId,
                        @NonNull String studentId,
                        @NonNull Consumer<Status<LessonEntity>> callback) {
        repository.markAttended(lessonId, studentId, callback);
    }
}
