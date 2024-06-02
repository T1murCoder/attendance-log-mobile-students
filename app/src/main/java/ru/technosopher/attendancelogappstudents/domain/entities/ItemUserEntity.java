package ru.technosopher.attendancelogappstudents.domain.entities;

import androidx.annotation.NonNull;

public class ItemUserEntity {
    @NonNull
    private final String id;
    @NonNull
    private final String username;

    public ItemUserEntity(@NonNull String id, @NonNull String name) {
        this.id = id;
        this.username = name;
    }


    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return username;
    }
}
