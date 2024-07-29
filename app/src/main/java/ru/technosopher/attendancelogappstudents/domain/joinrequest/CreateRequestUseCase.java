package ru.technosopher.attendancelogappstudents.domain.joinrequest;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.domain.entities.GroupJoinRequestEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.Status;

public class CreateRequestUseCase {

    private final JoinRequestRepository repository;

    public CreateRequestUseCase(JoinRequestRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String joinCode, @NonNull Consumer<Status<GroupJoinRequestEntity>> callback) {
        repository.createRequest(joinCode, callback);
    }

}
