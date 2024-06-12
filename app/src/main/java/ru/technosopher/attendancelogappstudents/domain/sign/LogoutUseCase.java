package ru.technosopher.attendancelogappstudents.domain.sign;

public class LogoutUseCase {
    private final SignUserRepository repository;

    public LogoutUseCase(SignUserRepository repository) {
        this.repository = repository;
    }

    public void execute(){
        repository.logout();
    }
}
