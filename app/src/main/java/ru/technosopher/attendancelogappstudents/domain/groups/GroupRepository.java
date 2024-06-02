package ru.technosopher.attendancelogappstudents.domain.groups;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.domain.entities.GroupEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.ItemGroupEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.Status;

public interface GroupRepository {
    void getGroupNameById(@NonNull String id, Consumer<Status<String>> callback);
    void getGroupByStudentId(@NonNull String id, Consumer<Status<String>> callback);
}
