package at.kaindorf.examdb.beans;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exam {

    private long examId;
    private int duration;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonAlias("dateofexam")
    private LocalDate dateOfExam;
    @JsonIgnore
    private Student student;

    @Override
    public String toString() {
        return "Exam{" +
                "examId=" + examId +
                ", duration=" + duration +
                ", dateOfExam=" + dateOfExam +
                ", student=" + student.getFirstname() +
                '}';
    }
}
