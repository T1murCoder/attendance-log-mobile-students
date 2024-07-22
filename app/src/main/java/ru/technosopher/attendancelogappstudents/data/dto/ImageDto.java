package ru.technosopher.attendancelogappstudents.data.dto;

import android.net.Uri;

import androidx.annotation.Nullable;

public class ImageDto {
    @Nullable
    public final Uri imageUri;

    public ImageDto(@Nullable Uri imageUri) {
        this.imageUri = imageUri;
    }
}
