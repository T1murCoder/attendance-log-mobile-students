package ru.technosopher.attendancelogappstudents.domain.entities;

public class GroupJoinRequestEntity {

    private final String id;

//    private final ItemStudentEntity student;


    public GroupJoinRequestEntity(String id) {
        this.id = id;
//        this.student = student;
    }

    public String getId() {
        return id;
    }

/*    public ItemStudentEntity getStudent() {
        return student;
    }*/
}
