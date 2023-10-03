package at.kaindorf.examdb.beans;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ClassList {

    private long classId;
    private String classname;
    private List<Student> students;

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "ClassList{" +
                "classId=" + classId +
                ", classname='" + classname + '\'' +
                ", students=" + students +
                '}';
    }
}
