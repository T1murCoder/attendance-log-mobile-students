package ru.technosopher.attendancelogappstudents.domain.users;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.domain.entities.Status;
import ru.technosopher.attendancelogappstudents.domain.entities.UserEntity;

public class UpdateUserProfileUseCase {

    private final UserRepository repository;

    public UpdateUserProfileUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String id, @NonNull UserEntity updatedUser, Consumer<Status<UserEntity>> callback) {
        repository.updateProfile(id, updatedUser, callback);
    }
}
