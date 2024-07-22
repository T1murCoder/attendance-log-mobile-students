package ru.technosopher.attendancelogappstudents.data.repository;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogappstudents.data.dto.UserRegisterDto;
import ru.technosopher.attendancelogappstudents.data.network.RetrofitFactory;
import ru.technosopher.attendancelogappstudents.data.source.CredentialsDataSource;
import ru.technosopher.attendancelogappstudents.data.source.UserApi;
import ru.technosopher.attendancelogappstudents.data.utils.CallToConsumer;
import ru.technosopher.attendancelogappstudents.data.utils.Mapper;
import ru.technosopher.attendancelogappstudents.domain.users.UserRepository;
import ru.technosopher.attendancelogappstudents.domain.entities.ItemUserEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.Status;
import ru.technosopher.attendancelogappstudents.domain.entities.UserAccountEntity;
import ru.technosopher.attendancelogappstudents.domain.entities.UserEntity;
import ru.technosopher.attendancelogappstudents.domain.sign.SignUserRepository;

public class UserRepositoryImpl implements UserRepository, SignUserRepository {
    private static UserRepositoryImpl INSTANCE;

    private UserApi userApi = RetrofitFactory.getInstance().getUserApi();

    private CredentialsDataSource credentialsDataSource = CredentialsDataSource.getInstance();

    private UserRepositoryImpl() {
    }

    public static synchronized UserRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserRepositoryImpl();
        }
        return INSTANCE;
    }


    @Override
    public void getAllUsers(@NonNull Consumer<Status<List<ItemUserEntity>>> callback) {

    }

    @Override
    public void updateProfile(@NonNull String id, @NonNull UserEntity updatedUser, Consumer<Status<UserEntity>> callback) {
        userApi.update(id, Mapper.fromUserEntityToDto(updatedUser)).enqueue(new CallToConsumer<>(
                callback,
                Mapper::fromUserDtoToEntity
        ));
    }

    @Override
    public void getUser(@NonNull String id, Consumer<Status<UserEntity>> callback) {
        userApi = RetrofitFactory.getInstance().getUserApi();
        userApi.login().enqueue(new CallToConsumer<>(
                callback,
                user -> {
                    if (user != null){
                        if (user.id != null && user.name != null && user.surname != null && user.username != null){
                            final String name = user.name;
                            final String surname = user.surname;
                            final String username = user.username;
                            return new UserEntity(
                                    id,
                                    name,
                                    surname,
                                    username,
                                    user.telegram_url,
                                    user.github_url,
                                    user.photo_url);
                        }
                    }
                    return null;
                }
        ));
    }

    @Override
    public void isExists(@NonNull String login, Consumer<Status<Void>> callback) {
        userApi.isExists(login).enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));
    }

    @Override
    public void registerUser(@NonNull String login,
                                @NonNull String password,
                                @NonNull String name,
                                @NonNull String surname, Consumer<Status<UserAccountEntity>> callback) {
        userApi.register(new UserRegisterDto(login, password, name, surname)).enqueue(new CallToConsumer<>(
                callback,
                userAcc -> {
                    if (userAcc != null){
                        credentialsDataSource.updateLogin(login, password);
                        return new UserAccountEntity(
                                userAcc.getId(),
                                userAcc.getName(),
                                userAcc.getSurname(),
                                userAcc.getUsername(),
                                password);
                    }
                    return null;
                }
        ));

    }

    @Override
    public void loginUser(@NonNull String login, @NonNull String password, Consumer<Status<UserEntity>> callback) {
        credentialsDataSource.updateLogin(login, password);
        userApi = RetrofitFactory.getInstance().getUserApi();
        userApi.login().enqueue(new CallToConsumer<>(
                callback,
                teacher -> {
                    if (teacher != null){
                        if (teacher.id != null && teacher.name != null && teacher.surname != null && teacher.username != null){
                            final String id = teacher.id;
                            final String name = teacher.name;
                            final String surname = teacher.surname;
                            final String username = teacher.username;
                            return new UserEntity(
                                    id,
                                    name,
                                    surname,
                                    username,
                                    teacher.telegram_url,
                                    teacher.github_url,
                                    teacher.photo_url);
                        }
                    }
                    return null;
                }
        ));

    }

    @Override
    public void logout() {
        credentialsDataSource.logout();
    }
}
