package ru.technosopher.attendancelogappstudents.data.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import ru.technosopher.attendancelogappstudents.data.dto.AttendanceDto;
import ru.technosopher.attendancelogappstudents.data.dto.StudentItemDto;
import ru.technosopher.attendancelogappstudents.data.dto.UserDto;
import ru.technosopher.attendancelogappstudents.domain.entities.AttendanceEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.ItemStudentEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.UserEntity;


public class Mapper {
    public static StudentItemDto fromEntityToDto(@NonNull ItemStudentEntity entity){
        StudentItemDto dto = new StudentItemDto();
        dto.id = entity.getId();
        dto.name = entity.getName();
        return dto;
    }

    public static List<StudentItemDto> fromEntityListToDtoList(@NonNull List<ItemStudentEntity> entityList){
        List<StudentItemDto> dtoList = new ArrayList<>();
        for (ItemStudentEntity entity: entityList) {
            dtoList.add(fromEntityToDto(entity));
        }
        return dtoList;
    }

    public static ItemStudentEntity fromDtoToEntity(@NonNull StudentItemDto dto){
        if (dto.id == null || dto.name == null || dto.surname == null) return new ItemStudentEntity("", "", "");
        return new ItemStudentEntity(dto.id, dto.name, dto.surname);
    }

    public static List<ItemStudentEntity> fromDtoListToEntityList(@NonNull List<StudentItemDto> dtoList){
        List<ItemStudentEntity> entityList = new ArrayList<>();
        for (StudentItemDto dto: dtoList) {
            entityList.add(fromDtoToEntity(dto));
        }
        return entityList;
    }

    public static List<AttendanceEntity> fromAttendanceDtoToAttendanceEntityList(@NonNull List<AttendanceDto> dtoList){
        List<AttendanceEntity> entityList = new ArrayList<>();
        for (AttendanceDto dto: dtoList) {
            entityList.add(fromAttendanceDtoToAttendanceEntity(dto));
        }
        return entityList;
    }

    public static UserDto fromUserEntityToDto(@NonNull UserEntity entity){
        return new UserDto(
                entity.getId(),
                entity.getName(),
                entity.getSurname(),
                entity.getUsername(),
                entity.getTelegram_url(),
                entity.getGithub_url(),
                entity.getPhoto_url());
    }

    public static UserEntity fromUserDtoToEntity(@NonNull UserDto dto){
        if (dto == null) return null;
        if (dto.id == null || dto.name == null || dto.surname == null || dto.username == null) return null;
        return new UserEntity(dto.id, dto.name, dto.surname, dto.username, dto.telegram_url, dto.github_url, dto.photo_url);
    }

    private static AttendanceEntity fromAttendanceDtoToAttendanceEntity(AttendanceDto dto) {
        if (dto.id == null || dto.isVisited == null || dto.lessonId == null || dto.studentId == null || dto.lessonTimeStart == null || dto.points == null) return new AttendanceEntity("", false, "", "", new GregorianCalendar(), "0");
        return new AttendanceEntity(dto.id, dto.isVisited, dto.studentId, dto.lessonId, dto.lessonTimeStart, dto.points);
    }

}
