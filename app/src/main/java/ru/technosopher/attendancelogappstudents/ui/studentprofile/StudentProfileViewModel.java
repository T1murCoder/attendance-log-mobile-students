package ru.technosopher.attendancelogappstudents.ui.studentprofile;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.technosopher.attendancelogappstudents.data.repository.StudentRepositoryImpl;
import ru.technosopher.attendancelogappstudents.domain.entities.UserEntity;
import ru.technosopher.attendancelogappstudents.domain.students.GetStudentProfileUseCase;

public class StudentProfileViewModel extends ViewModel {

    public static final String TAG = "STUDENT_PROFILE_VIEWMODEL";

    private final MutableLiveData<State> mutableStateLiveData = new MutableLiveData<>();
    public final LiveData<State> stateLiveData = mutableStateLiveData;

    private final GetStudentProfileUseCase getStudentProfileUseCase = new GetStudentProfileUseCase(
            StudentRepositoryImpl.getInstance()
    );

    @Nullable
    private String id;

    public void update() {
        // TODO: getStudentByIdUseCase
        if (id == null) {
            mutableStateLiveData.postValue(new State("Что-то пошло не так", null, false));
        } else {
            mutableStateLiveData.postValue(new State(null, null, true));
            getStudentProfileUseCase.execute(
                    id,
                    status -> {
                        if (status.getStatusCode() == 200 && status.getValue() != null) {
                            mutableStateLiveData.postValue(new State(null, status.getValue(), false));
                        } else {
                            Log.e(TAG, ""+status.getStatusCode());
                            mutableStateLiveData.postValue(new State("Что то пошло не так. Попробуйте еще раз", null, false));
                        }
                    }
            );
        }
    }

    public void saveStudentId(String id) {
        this.id = id;
    }

    public class State {
        @Nullable
        private final String errorMessage;
        @Nullable
        private final UserEntity student;
        @Nullable
        private final Boolean loading;
        public State(@Nullable String errorMessage, @Nullable UserEntity student, @Nullable Boolean loading) {
            this.errorMessage = errorMessage;
            this.student = student;
            this.loading = loading;
        }

        @Nullable
        public UserEntity getStudent() {
            return student;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @Nullable
        public Boolean getLoading() {
            return loading;
        }
    }
}
