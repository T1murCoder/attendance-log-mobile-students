package ru.technosopher.attendancelogappstudents.domain.joinrequest;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.domain.entities.GroupJoinRequestEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.Status;

public interface JoinRequestRepository {

    void getByStudentId(@NonNull String id, @NonNull Consumer<Status<GroupJoinRequestEntity>> callback);

    void declineRequest(@NonNull String requestId, @NonNull Consumer<Status<Void>> callback);

    void createRequest(@NonNull String joinCode, @NonNull Consumer<Status<GroupJoinRequestEntity>> callback);

}
