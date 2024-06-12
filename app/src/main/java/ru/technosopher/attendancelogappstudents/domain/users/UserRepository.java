package ru.technosopher.attendancelogappstudents.domain.users;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.domain.entities.ItemUserEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.Status;
import ru.technosopher.attendancelogappstudents.domain.entities.UserEntity;

public interface UserRepository {
    void getUser(@NonNull String id, Consumer<Status<UserEntity>> callback);
    void getAllUsers(@NonNull Consumer<Status<List<ItemUserEntity>>> callback);
    void updateProfile(@NonNull String id, @NonNull UserEntity updatedUser, Consumer<Status<UserEntity>> callback);
}
