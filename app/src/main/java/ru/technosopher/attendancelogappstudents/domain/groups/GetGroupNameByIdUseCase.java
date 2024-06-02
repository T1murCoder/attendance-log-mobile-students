package ru.technosopher.attendancelogappstudents.domain.groups;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.domain.entities.Status;


public class GetGroupNameByIdUseCase {
    private final GroupRepository repository;

    public GetGroupNameByIdUseCase(GroupRepository repository) {
        this.repository = repository;
    }
    public void execute(@NonNull String id, Consumer<Status<String>> callback){
        repository.getGroupNameById(id, callback);
    }
}

