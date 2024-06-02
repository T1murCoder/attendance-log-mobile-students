package ru.technosopher.attendancelogappstudents.domain.groups;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.domain.entities.ItemGroupEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.Status;

public class GetGroupByStudentIdUseCase {
    private final GroupRepository repository;

    public GetGroupByStudentIdUseCase(GroupRepository repository) {
        this.repository = repository;
    }
    public void execute(@NonNull String id, Consumer<Status<String >> callback){
        repository.getGroupByStudentId(id, callback);
    }
}
