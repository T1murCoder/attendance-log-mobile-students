package ru.technosopher.attendancelogappstudents.data.source;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.technosopher.attendancelogappstudents.data.dto.StudentDto;
import ru.technosopher.attendancelogappstudents.data.dto.StudentWithAttendances;
import ru.technosopher.attendancelogappstudents.data.dto.UserAccountDto;
import ru.technosopher.attendancelogappstudents.data.dto.UserDto;

public interface StudentApi {

    @GET("student/group/{id}")
    Call<List<StudentWithAttendances>> getStudentWithAttendancesByGroupId(@Path("id") String id);

    @GET("student/lesson/{id}")
    Call<List<StudentDto>> getStudentWithAttendancesByLessonId(@Path("id") String id);

    @GET("student/{id}")
    Call<UserDto> getStudentById(@Path("id") String id);
}
