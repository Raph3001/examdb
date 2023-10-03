package at.kaindorf.examdb.beans;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
public class Student {

    private long studentId;
    private String firstname;
    private String lastname;
    private List<Exam> exams;
    @JsonIgnore
    private ClassList classlist;

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }


    public ClassList getClasslist() {
        return classlist;
    }

    public void setClasslist(ClassList classlist) {
        this.classlist = classlist;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", exams=" + exams +
                ", classlist=" + classlist.getClassname() +
                '}';
    }
}
