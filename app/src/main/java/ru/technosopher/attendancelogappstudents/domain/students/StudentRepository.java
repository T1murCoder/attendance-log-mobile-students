package ru.technosopher.attendancelogappstudents.domain.students;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.domain.entities.Status;
import ru.technosopher.attendancelogappstudents.domain.entities.StudentEntity;

public interface StudentRepository {
    void getStudentsAttendances(@NonNull String id, Consumer<Status<List<StudentEntity>>> callback);

}
