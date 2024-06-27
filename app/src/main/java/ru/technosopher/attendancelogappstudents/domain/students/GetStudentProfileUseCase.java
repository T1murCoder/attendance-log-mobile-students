package ru.technosopher.attendancelogappstudents.domain.students;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.domain.entities.Status;
import ru.technosopher.attendancelogappstudents.domain.entities.UserEntity;

public class GetStudentProfileUseCase {

    private final StudentRepository repository;

    public GetStudentProfileUseCase(StudentRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String id, Consumer<Status<UserEntity>> callback) {
        repository.getStudentProfile(id, status -> {
            repository.getStudentProfile(id, callback);
        });
    }
}
