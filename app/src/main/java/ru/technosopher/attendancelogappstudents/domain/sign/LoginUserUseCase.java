package ru.technosopher.attendancelogappstudents.domain.sign;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.domain.entities.Status;
import ru.technosopher.attendancelogappstudents.domain.entities.UserEntity;

public class LoginUserUseCase {

    private final SignUserRepository repository;

    public LoginUserUseCase(SignUserRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String login, @NonNull String password, Consumer<Status<UserEntity>> callback) {
        repository.loginUser(login, password, status->{
            if (status.getStatusCode() != 200) repository.logout();
            callback.accept(status);
        });

    }
}
