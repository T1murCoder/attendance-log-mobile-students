package ru.technosopher.attendancelogappstudents.domain.sign;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.domain.entities.Status;
import ru.technosopher.attendancelogappstudents.domain.entities.UserAccountEntity;

public class RegisterUserUseCase {

    private final SignUserRepository repository;

    public RegisterUserUseCase(SignUserRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String login, @NonNull String password, @NonNull String name, @NonNull String surname, Consumer<Status<UserAccountEntity>> callback) {
        repository.registerUser(login, password, name, surname, callback);
    }
}
