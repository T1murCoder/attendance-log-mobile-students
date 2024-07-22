package ru.technosopher.attendancelogappstudents.ui.group;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import ru.technosopher.attendancelogappstudents.data.repository.GroupRepositoryImpl;
import ru.technosopher.attendancelogappstudents.data.repository.StudentRepositoryImpl;
import ru.technosopher.attendancelogappstudents.domain.entities.AttendanceEntity;

import ru.technosopher.attendancelogappstudents.domain.entities.GroupEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.StudentEntity;
import ru.technosopher.attendancelogappstudents.domain.groups.GetGroupByStudentIdUseCase;
import ru.technosopher.attendancelogappstudents.domain.students.GetStudentsAttendancesUseCase;
import ru.technosopher.attendancelogappstudents.ui.utils.DateFormatter;

public class GroupViewModel extends ViewModel {

    private static final String TAG = "GROUP_VIEW_MODEL";

    private final MutableLiveData<State> mutableStateLiveData = new MutableLiveData<>();
    public LiveData<State> stateLiveData = mutableStateLiveData;

    private final MutableLiveData<String> mutableErrorLiveData = new MutableLiveData<>();
    public LiveData<String> errorLiveData = mutableErrorLiveData;

    /* USE CASES */
    private final GetStudentsAttendancesUseCase getStudentsAttendancesUseCase = new GetStudentsAttendancesUseCase(
            StudentRepositoryImpl.getInstance()
    );

    private final GetGroupByStudentIdUseCase getGroupByStudentIdUseCase = new GetGroupByStudentIdUseCase(
            GroupRepositoryImpl.getInstance()
    );

    /* USE CASES */
    private GroupEntity group;

    private List<StudentEntity> students = new ArrayList<>();

    // Передаём айди ученика
    public void update(@Nullable String id) {

        getGroupByStudentIdUseCase.execute(id, groupStatus -> {
            Log.d(TAG, "" + id);
            Log.d(TAG, "" + groupStatus.getValue());
            Log.d(TAG, "" + groupStatus.getStatusCode());

            if (groupStatus.getStatusCode() == 200) {
                group = groupStatus.getValue();


                if (group != null) {
                    mutableStateLiveData.postValue(new State(null, null, null, false, true));
                    getStudentsAttendancesUseCase.execute(group.getId(), status -> {
                        List<StudentEntity> students = status.getValue() != null ? status.getValue() : null;
                        List<StudentEntity> sortedOrNullStudents = sortAttendancesForStudents(students);
                        List<StudentEntity> studentsByPoints = sortStudentsByPoints(sortedOrNullStudents);
                        Log.d(TAG, sortedOrNullStudents.toString());
                        Log.d(TAG, sortedOrNullStudents.get(0).getPoints());
                        this.students = studentsByPoints;
                        mutableStateLiveData.postValue(new State(group.getName(), studentsByPoints,
                                status.getErrors() != null ? status.getErrors().getLocalizedMessage() : null,
                                status.getErrors() == null && status.getValue() != null && !studentsByPoints.isEmpty(), false));
                    });
                } else {
                    mutableErrorLiveData.postValue("Вы не состоите в группе!");
                    group = null;
                }
            } else if (groupStatus.getStatusCode() == 404){
                mutableErrorLiveData.postValue("Вы не состоите в группе!");
                group = null;
            } else {
                mutableErrorLiveData.postValue("Что-то пошло не так. Попробуйте еще раз");
                group = null;
            }
        });
    }

    public List<String> extractDates(List<AttendanceEntity> attendances) {
        List<String> dates = new ArrayList<>();
        sortAttendances(attendances);
        for (AttendanceEntity att : attendances) {
            dates.add(DateFormatter.getDateStringFromDate(att.getLessonTimeStart(), "MMM dd"));
        }
        return dates;
    }
    public List<StudentEntity> getStudents() {
        return this.students;
    }

    public GroupEntity getGroup() {
        return group;
    }

    // Фильтруем посещения по месяцам
//    public void filterGroupByMonth(@Nullable Calendar month) {
//        Log.d(TAG, String.valueOf(students));
//        if (students == null || group == null) return;
//
//        if (month == null) {
//            mutableStateLiveData.postValue(new State(group.getName(), students,
//                    null,
//                    true, false));
//        } else {
////            List<StudentEntity> filteredStudents = students.stream().map(
////                    studentEntity -> studentEntity.setAttendanceEntityList(
////                            studentEntity.getAttendanceEntityList().stream().filter(
////                                    attendanceEntity -> attendanceEntity.getLessonTimeStart().get(Calendar.MONTH) == month.get(Calendar.MONTH)
////                            ).collect(Collectors.toList())
////                    )
////            ).collect(Collectors.toList());
//            List<StudentEntity> filteredStudents = new ArrayList<>();
//            for (StudentEntity student : students) {
//                // ТУТ ФИЛЬТРУЕМ Attendances
//                StudentEntity studentWithFilteredAttendances = new StudentEntity(student);
//                studentWithFilteredAttendances.setAttendanceEntityList(
//                        studentWithFilteredAttendances.getAttendanceEntityList().stream()
//                                .filter(
//                                        attendanceEntity -> attendanceEntity.getLessonTimeStart().get(Calendar.MONTH) == month.get(Calendar.MONTH)
//                                )
//                                .collect(Collectors.toList())
//                );
//                filteredStudents.add(studentWithFilteredAttendances);
//            }
//            mutableStateLiveData.postValue(new State(group.getName(), filteredStudents,
//                    null, true, false));
//        }
//    }

    private List<StudentEntity> sortAttendancesForStudents(@Nullable List<StudentEntity> students) {
        if (students == null) return new ArrayList<>();
        for (StudentEntity student : students) {
            List<AttendanceEntity> attendanceEntities = student.getAttendanceEntityList();
            attendanceEntities.sort(Comparator.comparing(AttendanceEntity::getLessonTimeStart));
        }
        return students;
    }

    private List<StudentEntity> sortStudentsByPoints(@Nullable List<StudentEntity> students) {
        if (students == null) return new ArrayList<>();

        return students.stream()
                .sorted(Comparator.comparing(studentEntity -> -Integer.parseInt(studentEntity.getPoints())))
                .collect(Collectors.toList());
    }

    private void sortAttendances(@Nullable List<AttendanceEntity> attendances) {
        if (attendances == null) return;
        attendances.sort(Comparator.comparing(AttendanceEntity::getLessonTimeStart));
    }

    public class State {

        @Nullable
        private final String groupName;
        @Nullable
        private final List<StudentEntity> students;
        @Nullable
        private final String errorMessage;
        @NonNull
        private final Boolean isSuccess;
        @NonNull
        private final Boolean isLoading;

        public State(@Nullable String groupName, @Nullable List<StudentEntity> students, @Nullable String errorMessage, @NonNull Boolean isSuccess, @NonNull Boolean isLoading) {
            this.groupName = groupName;
            this.students = students;
            this.errorMessage = errorMessage;
            this.isSuccess = isSuccess;
            this.isLoading = isLoading;
        }

        @Nullable
        public List<StudentEntity> getStudents() {
            return students;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @NonNull
        public Boolean getSuccess() {
            return isSuccess;
        }

        @NonNull
        public Boolean getLoading() {
            return isLoading;
        }

        @Nullable
        public String getGroupName() {
            return groupName;
        }
    }
}
