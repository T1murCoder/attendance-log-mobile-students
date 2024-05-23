package ru.technosopher.attendancelogappstudents.domain;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.domain.entities.Status;
import ru.technosopher.attendancelogappstudents.domain.entities.UserEntity;

public class GetUserByIdUseCase {
    private final UserRepository repository;

    public GetUserByIdUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String id, Consumer<Status<UserEntity>> callback) {
        repository.getUser(id, callback);
    }
}
