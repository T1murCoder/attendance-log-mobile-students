package ru.technosopher.attendancelogappstudents.data.source;

import java.util.List;

import retrofit2.Call;
import ru.technosopher.attendancelogappstudents.data.dto.StudentDto;

public interface StudentApi {

    Call<List<StudentDto>> getStudentListByGroupId();
}
