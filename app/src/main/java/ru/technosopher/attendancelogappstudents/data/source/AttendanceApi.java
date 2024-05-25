package ru.technosopher.attendancelogappstudents.data.source;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AttendanceApi {
    @PUT("attendance/{id}")
    Call<Void> addAttendance(@Path("id") Long id);
}
