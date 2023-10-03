package at.kaindorf.examdb;

import at.kaindorf.examdb.beans.ClassList;
import at.kaindorf.examdb.beans.Exam;
import at.kaindorf.examdb.beans.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
@Getter
public class ExamMockDB {

    private List<ClassList> classes;

    @PostConstruct
    public void initMockDB() {
        log.info("Loading mockdatabase");
        InputStream is = this.getClass().getResourceAsStream("/examdb.json");
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        try {
            classes = mapper.readerForListOf(ClassList.class).readValue(is);
            classes.forEach(c -> c.getStudents().forEach(s -> s.setClasslist(c)));
            log.info("json-data successfully loaded");
            AtomicInteger i = new AtomicInteger(0);
            classes.forEach(c -> {
                c.getStudents().forEach(s -> {
                    i.getAndIncrement();
                    s.setStudentId(i.get());
                    s.getExams().forEach(e -> {
                        e.setStudent(s);
                    });
                });
            });
            System.out.println(classes);
        } catch (IOException e) {
            log.error("reading json-data failed - " + e.toString());
        }
    }

    public List<Student> getAllStudents() {
        List<Student> toReturn = new ArrayList<>();
        classes.forEach(c -> {
            toReturn.addAll(c.getStudents());
        });
        System.out.println(toReturn);
        return toReturn;
    }

    public List<ClassList> getAllClasses() {
        return classes;
    }

}
