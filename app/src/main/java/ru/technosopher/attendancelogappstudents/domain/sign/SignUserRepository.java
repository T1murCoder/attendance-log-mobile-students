package ru.technosopher.attendancelogappstudents.domain.sign;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.domain.entities.Status;
import ru.technosopher.attendancelogappstudents.domain.entities.UserAccountEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.UserEntity;

public interface SignUserRepository {
    void isExists(@NonNull String login, Consumer<Status<Void>> callback);

    void registerUser(@NonNull String login,
                         @NonNull String password,
                         @NonNull String name,
                         @NonNull String surname,
                         Consumer<Status<UserAccountEntity>> callback);

    void loginUser(@NonNull String login, @NonNull String password, Consumer<Status<UserEntity>> callback);

    void logout();
}
