package ru.technosopher.attendancelogappstudents.data.dto;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class GroupJoinRequestDto {

    @Nullable
    @SerializedName("id")
    public String id;

    @Nullable
    @SerializedName("student")
    public StudentDto student;

    public GroupJoinRequestDto(@Nullable String id, @Nullable StudentDto student) {
        this.id = id;
        this.student = student;
    }
}
