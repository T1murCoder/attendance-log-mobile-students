package ru.technosopher.attendancelogappstudents.ui.group;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import ru.technosopher.attendancelogappstudents.data.repository.GroupRepositoryImpl;
import ru.technosopher.attendancelogappstudents.data.repository.JoinRequestRepositoryImpl;
import ru.technosopher.attendancelogappstudents.data.repository.StudentRepositoryImpl;
import ru.technosopher.attendancelogappstudents.domain.entities.AttendanceEntity;

import ru.technosopher.attendancelogappstudents.domain.entities.GroupEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.GroupJoinRequestEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.Status;
import ru.technosopher.attendancelogappstudents.domain.entities.StudentEntity;
import ru.technosopher.attendancelogappstudents.domain.groups.GetGroupByStudentIdUseCase;
import ru.technosopher.attendancelogappstudents.domain.joinrequest.CreateRequestUseCase;
import ru.technosopher.attendancelogappstudents.domain.joinrequest.DeclineRequestUseCase;
import ru.technosopher.attendancelogappstudents.domain.joinrequest.GetRequestByStudentIdUseCase;
import ru.technosopher.attendancelogappstudents.domain.students.GetStudentsAttendancesUseCase;
import ru.technosopher.attendancelogappstudents.ui.utils.DateFormatter;

public class GroupViewModel extends ViewModel {

    private static final String TAG = "GROUP_VIEW_MODEL";

    private final MutableLiveData<State> mutableStateLiveData = new MutableLiveData<>();
    public LiveData<State> stateLiveData = mutableStateLiveData;

    private final MutableLiveData<String> mutableErrorLiveData = new MutableLiveData<>();
    public LiveData<String> errorLiveData = mutableErrorLiveData;

    private final MutableLiveData<String> mutableInteractionLiveData = new MutableLiveData<>();
    public LiveData<String> interactionLiveData = mutableInteractionLiveData;

    /* USE CASES */
    private final GetStudentsAttendancesUseCase getStudentsAttendancesUseCase = new GetStudentsAttendancesUseCase(
            StudentRepositoryImpl.getInstance()
    );

    private final GetGroupByStudentIdUseCase getGroupByStudentIdUseCase = new GetGroupByStudentIdUseCase(
            GroupRepositoryImpl.getInstance()
    );

    private final GetRequestByStudentIdUseCase getRequestByStudentIdUseCase = new GetRequestByStudentIdUseCase(
            JoinRequestRepositoryImpl.getInstance()
    );

    private final CreateRequestUseCase createRequestUseCase = new CreateRequestUseCase(
            JoinRequestRepositoryImpl.getInstance()
    );

    private final DeclineRequestUseCase declineRequestUseCase = new DeclineRequestUseCase(
            JoinRequestRepositoryImpl.getInstance()
    );

    /* USE CASES */
    private GroupEntity group;
    private static GroupJoinRequestEntity joinRequest;

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
                    mutableStateLiveData.postValue(new State(null, null, null, false, true, null, true));
                    getStudentsAttendancesUseCase.execute(group.getId(), status -> {
                        List<StudentEntity> students = status.getValue() != null ? status.getValue() : null;
                        List<StudentEntity> sortedOrNullStudents = sortAttendancesForStudents(students);
                        List<StudentEntity> studentsByPoints = sortStudentsByPoints(sortedOrNullStudents);
                        Log.d(TAG, sortedOrNullStudents.toString());
                        Log.d(TAG, sortedOrNullStudents.get(0).getPoints());
                        this.students.clear();
                        this.students.addAll(studentsByPoints);
                        mutableStateLiveData.postValue(new State(group.getName(), this.students,
                                status.getErrors() != null ? status.getErrors().getLocalizedMessage() : null,
                                status.getErrors() == null && status.getValue() != null && !this.students.isEmpty(), true, null, false));
                    });
                } else {
                    mutableErrorLiveData.postValue("Что-то пошло не так. Попробуйте еще раз1");
                    group = null;
                }
            } else if (groupStatus.getStatusCode() == 404) {

                getRequestByStudentIdUseCase.execute(id, this::processRequestByStudentIdUseCase);
                group = null;
            } else {
                mutableErrorLiveData.postValue("Что-то пошло не так. Попробуйте еще раз2");
                group = null;
            }
        });
    }

    public void createJoinRequest(String joinCode) {
        createRequestUseCase.execute(joinCode, status -> {
            Log.d(TAG, String.valueOf(status.getStatusCode()) + " createJoinRequest");
            if (status.getStatusCode() == 200) {
                mutableInteractionLiveData.postValue("Запрос отправлен!");
            } else {
                mutableInteractionLiveData.postValue("Что-то пошло не так");
            }
        });
    }

    public void cancelJoinRequest(String studentId) {
        declineRequestUseCase.execute(studentId, status -> {
            Log.d(TAG, String.valueOf(status.getStatusCode()) + " cancelJoinRequest");
            if (status.getStatusCode() == 200) {
                mutableInteractionLiveData.postValue("Запрос отменен");
            } else {
                mutableInteractionLiveData.postValue("Что-то пошло не так");
            }
        });
    }

    public List<String> extractDates(List<AttendanceEntity> attendances) {
        List<String> dates = new ArrayList<>();
        sortAttendances(attendances);
        for (AttendanceEntity att : attendances) {
            dates.add(DateFormatter.getDateStringFromDate(att.getLessonTimeStart(), "MMM\ndd"));
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
    public void filterGroupByMonth(@Nullable Calendar month) {
        Log.d(TAG, String.valueOf(students));
        if (students == null || group == null) return;

        if (month == null) {
            mutableStateLiveData.postValue(new State(group.getName(), students,
                    null,
                    true, true, null, false));
        } else {
//            List<StudentEntity> filteredStudents = students.stream().map(
//                    studentEntity -> studentEntity.setAttendanceEntityList(
//                            studentEntity.getAttendanceEntityList().stream().filter(
//                                    attendanceEntity -> attendanceEntity.getLessonTimeStart().get(Calendar.MONTH) == month.get(Calendar.MONTH)
//                            ).collect(Collectors.toList())
//                    )
//            ).collect(Collectors.toList());
            List<StudentEntity> filteredStudents = new ArrayList<>();
            for (StudentEntity student : students) {
                // ТУТ ФИЛЬТРУЕМ Attendances
                StudentEntity studentWithFilteredAttendances = new StudentEntity(student);
                studentWithFilteredAttendances.setAttendanceEntityList(
                        studentWithFilteredAttendances.getAttendanceEntityList().stream()
                                .filter(
                                        attendanceEntity -> attendanceEntity.getLessonTimeStart().get(Calendar.MONTH) == month.get(Calendar.MONTH)
                                )
                                .collect(Collectors.toList())
                );
                filteredStudents.add(studentWithFilteredAttendances);
            }
            mutableStateLiveData.postValue(new State(group.getName(), filteredStudents,
                    null, true, true, null, false));
        }
    }

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

    private void processRequestByStudentIdUseCase(Status<GroupJoinRequestEntity> status) {
        Log.d(TAG, String.valueOf(status.getStatusCode()) + " processRequestByStudentIdUseCase");
        if (status.getStatusCode() == 200) {
            joinRequest = status.getValue();

            if (joinRequest != null) {
                mutableStateLiveData.postValue(new State(null, null, null, false, false, true, false));
            } else {
                Log.d(TAG, "REQUEST IS NULL");
                mutableErrorLiveData.postValue("Что-то пошло не так. Попробуйте еще раз3");
                joinRequest = null;
            }

        } else if (status.getStatusCode() == 404) {
            mutableStateLiveData.postValue(new State(null, null, null, false, false, false, false));
            joinRequest = null;
        } else {
            mutableErrorLiveData.postValue("Что-то пошло не так. Попробуйте еще раз4");
            group = null;
        }
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

        // Отвечает за то, какой мы экран показываем
        @NonNull
        private final Boolean isGroupExists;

        // Отвечает за то, какую мы кнопку показываем
        @Nullable
        private final Boolean isRequestExists;
        @NonNull
        private final Boolean isLoading;

        public State(@Nullable String groupName, @Nullable List<StudentEntity> students, @Nullable String errorMessage, @NonNull Boolean isSuccess, @NonNull Boolean isGroupExists, @Nullable Boolean isRequestExists, @NonNull Boolean isLoading) {
            this.groupName = groupName;
            this.students = students;
            this.errorMessage = errorMessage;
            this.isSuccess = isSuccess;
            this.isGroupExists = isGroupExists;
            this.isRequestExists = isRequestExists;
            this.isLoading = isLoading;
        }

        @Nullable
        public List<StudentEntity> getStudents() {
            return students;
        }

        @Nullable
        public Boolean getRequestExists() {
            return isRequestExists;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @NonNull
        public Boolean getGroupExists() {
            return isGroupExists;
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
