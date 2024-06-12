package ru.technosopher.attendancelogappstudents.domain.sign;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.domain.entities.Status;

public class IsUserExistsUseCase {

    private final SignUserRepository repository;

    public IsUserExistsUseCase(SignUserRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String login, Consumer<Status<Boolean>> callback){
        repository.isExists(login, status -> {
            boolean isAvailable = status.getStatusCode() == 200 || status.getStatusCode() == 404;
            callback.accept(new Status<>(
                    status.getStatusCode(),
                    isAvailable ? status.getStatusCode() == 200 : null,
                    status.getErrors()
            ));
        });
    }
}
