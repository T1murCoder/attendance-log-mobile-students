package ru.technosopher.attendancelogappstudents.data;

import androidx.annotation.NonNull;

import java.util.GregorianCalendar;
import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.data.network.RetrofitFactory;
import ru.technosopher.attendancelogappstudents.data.source.LessonApi;
import ru.technosopher.attendancelogappstudents.data.utils.CallToConsumer;
import ru.technosopher.attendancelogappstudents.domain.entities.LessonEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.Status;
import ru.technosopher.attendancelogappstudents.domain.lessons.LessonRepository;

public class LessonRepositoryImpl implements LessonRepository {

    private static LessonRepositoryImpl INSTANCE;

    private final LessonApi lessonApi = RetrofitFactory.getInstance().getLessonApi();

    public static LessonRepositoryImpl getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new LessonRepositoryImpl();
        return INSTANCE;
    }

    @Override
    public void getLessonById(@NonNull String id, @NonNull Consumer<Status<LessonEntity>> callback) {
        lessonApi.getLessonById(id).enqueue(new CallToConsumer<>(
                callback,
                lessonDto -> {
                    if (lessonDto != null) {
                        final String _id = lessonDto.id;
                        final String theme = lessonDto.theme;
                        final String groupId = lessonDto.groupId;
                        final String groupName = lessonDto.groupName;
                        final GregorianCalendar timeStart = lessonDto.timeStart;
                        final GregorianCalendar timeEnd = lessonDto.timeEnd;
                        final GregorianCalendar date = lessonDto.date;
                        if (_id != null && theme != null && groupId != null && groupName != null && timeStart != null && timeEnd != null && date != null) {
                            return new LessonEntity(_id, theme, groupId, groupName, timeStart, timeEnd, date);
                        }
                    }
                    return null;
                }
        ));
    }

    @Override
    public void markAttendedByLessonId(@NonNull String lessonId, @NonNull String studentId, @NonNull Consumer<Status<LessonEntity>> callback) {
        lessonApi.markAttendedByLessonId(lessonId, studentId).enqueue(new CallToConsumer<>(
                callback,
                lessonDto -> {
                    if (lessonDto != null) {
                        final String _id = lessonDto.id;
                        final String theme = lessonDto.theme;
                        final String groupId = lessonDto.groupId;
                        final String groupName = lessonDto.groupName;
                        final GregorianCalendar timeStart = lessonDto.timeStart;
                        final GregorianCalendar timeEnd = lessonDto.timeEnd;
                        final GregorianCalendar date = lessonDto.date;
                        if (_id != null && theme != null && groupId != null && groupName != null && timeStart != null && timeEnd != null && date != null) {
                            return new LessonEntity(_id, theme, groupId, groupName, timeStart, timeEnd, date);
                        }
                    }
                    return null;
                }
        ));
    }

    @Override
    public void markAttendedByQRCodeId(@NonNull String qrCodeId, @NonNull String studentId, @NonNull Consumer<Status<LessonEntity>> callback) {
        lessonApi.markAttendedByQRCodeId(qrCodeId, studentId).enqueue(new CallToConsumer<>(
                callback,
                lessonDto -> {
                    if (lessonDto != null) {
                        final String _id = lessonDto.id;
                        final String theme = lessonDto.theme;
                        final String groupId = lessonDto.groupId;
                        final String groupName = lessonDto.groupName;
                        final GregorianCalendar timeStart = lessonDto.timeStart;
                        final GregorianCalendar timeEnd = lessonDto.timeEnd;
                        final GregorianCalendar date = lessonDto.date;
                        if (_id != null && theme != null && groupId != null && groupName != null && timeStart != null && timeEnd != null && date != null) {
                            return new LessonEntity(_id, theme, groupId, groupName, timeStart, timeEnd, date);
                        }
                    }
                    return null;
                }
        ));
    }
}
