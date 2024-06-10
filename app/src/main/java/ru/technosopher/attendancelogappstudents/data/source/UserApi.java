package ru.technosopher.attendancelogappstudents.data.source;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ru.technosopher.attendancelogappstudents.data.dto.UserAccountDto;
import ru.technosopher.attendancelogappstudents.data.dto.UserDto;
import ru.technosopher.attendancelogappstudents.data.dto.UserRegisterDto;

public interface UserApi {
    @GET("student/login")
    Call<UserDto> login();
    @GET("student/username/{username}")
    Call<Void> isExists(@Path("username") String login);
    @POST("student/register")
    Call<UserAccountDto> register(@Body UserRegisterDto dto);

    @PUT("student/{id}")
    Call<UserDto> update(@Path("id") String id, @Body UserDto dto);
}
