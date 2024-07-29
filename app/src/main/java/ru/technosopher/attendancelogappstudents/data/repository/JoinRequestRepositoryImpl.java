package ru.technosopher.attendancelogappstudents.data.repository;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.data.network.RetrofitFactory;
import ru.technosopher.attendancelogappstudents.data.source.JoinRequestApi;
import ru.technosopher.attendancelogappstudents.data.utils.CallToConsumer;
import ru.technosopher.attendancelogappstudents.domain.entities.GroupJoinRequestEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.Status;
import ru.technosopher.attendancelogappstudents.domain.joinrequest.JoinRequestRepository;

public class JoinRequestRepositoryImpl implements JoinRequestRepository {

    private static JoinRequestRepositoryImpl INSTANCE;

    public static JoinRequestRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JoinRequestRepositoryImpl();
        }
        return INSTANCE;
    }

    private JoinRequestApi requestApi = RetrofitFactory.getInstance().getJoinRequestApi();

    @Override
    public void getByStudentId(@NonNull String id, @NonNull Consumer<Status<GroupJoinRequestEntity>> callback) {
        requestApi.getByStudentId(id).enqueue(new CallToConsumer<>(
                callback,
                groupJoinRequestDto -> {
                    if (groupJoinRequestDto != null) {
                        final String _id = groupJoinRequestDto.id;
                        if (_id != null) {
                            return new GroupJoinRequestEntity(_id);
                        }
                    }
                    return null;
                }
        ));
    }

    @Override
    public void declineRequest(@NonNull String requestId, @NonNull Consumer<Status<Void>> callback) {
        requestApi.declineRequestByStudentId(requestId).enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));
    }

    @Override
    public void createRequest(@NonNull String joinCode, @NonNull Consumer<Status<GroupJoinRequestEntity>> callback) {
        requestApi.createRequest(joinCode).enqueue(new CallToConsumer<>(
                callback,
                groupJoinRequestDto -> {
                    if (groupJoinRequestDto != null) {
                        final String _id = groupJoinRequestDto.id;
                        if (_id != null) {
                            return new GroupJoinRequestEntity(_id);
                        }
                    }
                    return null;
                }
        ));
    }
}
