package ru.technosopher.attendancelogappstudents.data.source;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ru.technosopher.attendancelogappstudents.data.dto.GroupDto;

public interface GroupApi {
    @GET("group/{id}")
    Call<GroupDto> getGroupNameById(@Path("id") String id);

//    @GET("group/student/{id}")
//    Call<GroupDto> getGroupByStudentId(@Path("id") String id);
    @GET("group/student/{id}")
    Call<String> getGroupByStudentId(@Path("id") String id);
}
