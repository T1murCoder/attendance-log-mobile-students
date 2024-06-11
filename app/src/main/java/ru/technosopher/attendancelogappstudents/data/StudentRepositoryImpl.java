package ru.technosopher.attendancelogappstudents.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.data.dto.StudentWithAttendances;
import ru.technosopher.attendancelogappstudents.data.network.RetrofitFactory;
import ru.technosopher.attendancelogappstudents.data.source.StudentApi;
import ru.technosopher.attendancelogappstudents.data.utils.CallToConsumer;
import ru.technosopher.attendancelogappstudents.data.utils.Mapper;
import ru.technosopher.attendancelogappstudents.domain.entities.AttendanceEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.Status;

import ru.technosopher.attendancelogappstudents.domain.entities.StudentEntity;
import ru.technosopher.attendancelogappstudents.domain.students.StudentRepository;

public class StudentRepositoryImpl implements StudentRepository {
    private static StudentRepositoryImpl INSTANCE;

    private final StudentApi studentApi = RetrofitFactory.getInstance().getStudentApi();

    public static StudentRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StudentRepositoryImpl();
        }
        return INSTANCE;
    }

    @Override
    public void getStudentsAttendances(@NonNull String groupId, Consumer<Status<List<StudentEntity>>> callback) {
        studentApi.getStudentWithAttendancesByGroupId(groupId).enqueue(new CallToConsumer<>(
                callback,
                studentsDto -> {
                    if (studentsDto != null){
                        ArrayList<StudentEntity> res = new ArrayList<>();
                        for (StudentWithAttendances dto: studentsDto){
                            final String id = dto.id;
                            final String name = dto.name;
                            final String surname = dto.surname;
                            final String points = dto.points;
                            assert dto.attendanceDtoList != null;
                            List<AttendanceEntity> attendanceEntityList = Mapper.fromAttendanceDtoToAttendanceEntityList(dto.attendanceDtoList);
                            if (id != null && name != null && surname != null && points != null){
                                res.add(new StudentEntity(id, name, surname, points, attendanceEntityList));
                            }
                        }
//                        System.out.println(res);
                        return res;
                    }
                    return null;
                }
        ));
    }
}
