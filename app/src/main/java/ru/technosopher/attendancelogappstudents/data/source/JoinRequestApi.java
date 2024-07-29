package ru.technosopher.attendancelogappstudents.data.source;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ru.technosopher.attendancelogappstudents.data.dto.GroupJoinRequestDto;

public interface JoinRequestApi {

    @POST("/join_request/{join_code}")
    Call<GroupJoinRequestDto> createRequest(@Path("join_code") String joinCode);

    @GET("/join_request/student/{student_id}")
    Call<GroupJoinRequestDto> getByStudentId(@Path("student_id") String studentId);

    @PUT("/join_request/student/{student_id}/decline")
    Call<Void> declineRequestByStudentId(@Path("student_id") String studentId);

}
