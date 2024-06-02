package ru.technosopher.attendancelogappstudents.data.source;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ru.technosopher.attendancelogappstudents.data.dto.LessonDto;

public interface LessonApi {
    @GET("lesson/{id}")
    Call<LessonDto> getLessonById(@Path("id") String id);

    @PUT("lesson/attendance/{lesson_id}/{student_id}")
    Call<LessonDto> markAttended(@Path("lesson_id") String lessonId, @Path("student_id") String studentId);
}
