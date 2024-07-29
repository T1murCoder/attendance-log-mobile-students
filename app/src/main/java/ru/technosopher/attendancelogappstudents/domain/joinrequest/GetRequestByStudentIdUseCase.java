package ru.technosopher.attendancelogappstudents.domain.joinrequest;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.domain.entities.GroupJoinRequestEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.Status;

public class GetRequestByStudentIdUseCase {

    private final JoinRequestRepository repository;

    public GetRequestByStudentIdUseCase(JoinRequestRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String id, @NonNull Consumer<Status<GroupJoinRequestEntity>> callback) {
        repository.getByStudentId(id, callback);
    }
}
