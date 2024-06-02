package ru.technosopher.attendancelogappstudents.data;

import androidx.annotation.NonNull;

import java.util.function.Consumer;
import java.util.stream.Collectors;

import ru.technosopher.attendancelogappstudents.data.network.RetrofitFactory;
import ru.technosopher.attendancelogappstudents.data.source.GroupApi;
import ru.technosopher.attendancelogappstudents.data.utils.CallToConsumer;
import ru.technosopher.attendancelogappstudents.data.utils.Mapper;
import ru.technosopher.attendancelogappstudents.domain.entities.GroupEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.ItemGroupEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.Status;
import ru.technosopher.attendancelogappstudents.domain.groups.GroupRepository;

public class GroupRepositoryImpl implements GroupRepository {

    private static GroupRepositoryImpl INSTANCE;
    private GroupApi groupApi = RetrofitFactory.getInstance().getGroupApi();

    public static GroupRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GroupRepositoryImpl();
        }
        return INSTANCE;
    }

    @Override
    public void getGroupNameById(@NonNull String id, Consumer<Status<String>> callback) {
        groupApi.getGroupNameById(id).enqueue(new CallToConsumer<>(
                callback,
                group -> {
                    if (group != null) {
                        if (group.id != null && group.name != null) return group.name;
                    }
                    return null;
                }
        ));
    }

    @Override
    public void getGroupByStudentId(@NonNull String id, Consumer<Status<String>> callback) {
        groupApi.getGroupByStudentId(id).enqueue(new CallToConsumer<>(
                callback,
                group -> {
                    if (group != null) {
                        return group;
                    }
                    return null;
                }
        ));
    }
}
