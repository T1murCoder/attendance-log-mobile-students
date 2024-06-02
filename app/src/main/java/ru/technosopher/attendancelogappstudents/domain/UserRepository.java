package ru.technosopher.attendancelogappstudents.domain;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.domain.entities.ItemUserEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.Status;
import ru.technosopher.attendancelogappstudents.domain.entities.UserEntity;

public interface UserRepository {
    void getUser(@NonNull String id, Consumer<Status<UserEntity>> callback);
    void getAllUsers(@NonNull Consumer<Status<List<ItemUserEntity>>> callback);
}
