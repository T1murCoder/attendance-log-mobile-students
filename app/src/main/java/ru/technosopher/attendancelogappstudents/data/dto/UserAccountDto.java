package ru.technosopher.attendancelogappstudents.data.dto;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class UserAccountDto {
    @NonNull
    @SerializedName("id")
    public String id;
    @NonNull
    @SerializedName("name")
    public String name;
    @NonNull
    @SerializedName("surname")
    public String surname;
    @NonNull
    @SerializedName("username")
    public String username;
    @NonNull
    @SerializedName("password")
    public String password;

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getSurname() {
        return surname;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public UserAccountDto(@NonNull String id, @NonNull String username, @NonNull String password, @NonNull String name, @NonNull String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }
}
