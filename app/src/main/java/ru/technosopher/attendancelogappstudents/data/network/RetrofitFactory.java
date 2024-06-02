package ru.technosopher.attendancelogappstudents.data.network;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.technosopher.attendancelogappstudents.data.source.AttendanceApi;
import ru.technosopher.attendancelogappstudents.data.source.CredentialsDataSource;
import ru.technosopher.attendancelogappstudents.data.source.GroupApi;
import ru.technosopher.attendancelogappstudents.data.source.LessonApi;
import ru.technosopher.attendancelogappstudents.data.source.StudentApi;
import ru.technosopher.attendancelogappstudents.data.source.UserApi;

public class RetrofitFactory {

    private static RetrofitFactory INSTANCE;

    private RetrofitFactory() {
    }

    public static synchronized RetrofitFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RetrofitFactory();
        }
        return INSTANCE;
    }

    private final OkHttpClient.Builder client = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                        String authData = CredentialsDataSource.getInstance().getAuthData();
                        if (authData == null) {
                            return chain.proceed(chain.request());
                        } else {
                            Request request = chain.request()
                                    .newBuilder()
                                    .addHeader("Authorization", authData)
                                    .build();
                            return chain.proceed(request);
                        }
                    }
            );

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(GregorianCalendar.class, new DateTypeAdapter())
            .create();

    private Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl("http://192.168.1.5:8080/")
            .baseUrl("http://193.164.149.209:8080/")
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public UserApi getUserApi() {return retrofit.create(UserApi.class);}

    public AttendanceApi getAttendanceApi() {return retrofit.create(AttendanceApi.class);}

    public LessonApi getLessonApi() {return retrofit.create(LessonApi.class);}

    public GroupApi getGroupApi() {return retrofit.create(GroupApi.class);}

    public StudentApi getStudentApi() { return retrofit.create(StudentApi.class); }


    public class DateTypeAdapter implements JsonDeserializer<GregorianCalendar>, JsonSerializer<GregorianCalendar> {

        @SuppressLint("SimpleDateFormat")
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        @Override
        public GregorianCalendar deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            synchronized (dateFormat){
                try{
                    Date date = dateFormat.parse(json.getAsString());
                    GregorianCalendar calendar = new GregorianCalendar();
                    calendar.setTime(date);
                    return calendar;
                }catch (ParseException e){
                    throw new JsonParseException(e);
                }

            }
        }

        @Override
        public JsonElement serialize(GregorianCalendar src, Type typeOfT, JsonSerializationContext context){
            synchronized (dateFormat){
                return context.serialize(dateFormat.format(src.getTime()));
            }
        }
    }
}
