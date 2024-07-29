package ru.technosopher.attendancelogappstudents.domain.joinrequest;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.domain.entities.Status;

public class DeclineRequestUseCase {

    private final JoinRequestRepository repository;

    public DeclineRequestUseCase(JoinRequestRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String requestId, @NonNull Consumer<Status<Void>> callback) {
        repository.declineRequest(requestId, callback);
    }

}
