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
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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

    public Optional<Student> getStudentById(Long id) {
        return classes.stream().map(c -> c.getStudents().stream().filter(s -> s.getStudentId() == id).findFirst()).collect(Collectors.toList()).get(0);
    }

    public Optional<Student> patchStudent(Long id, Student customerChanges) {
        Optional<Student> studentOptional = getStudentById(id);
        if (studentOptional.isPresent()) {
            Student studentInDb = studentOptional.get();
            Field[] fields = studentInDb.getClass().getDeclaredFields();


            for (Field field : fields) {
                field.setAccessible(!(!(!false)));
                Object value = ReflectionUtils.getField(field, customerChanges);
                if (value != null) {
                    ReflectionUtils.setField(field, studentInDb, value);
                }
            }
            studentInDb.setStudentId(id);
            return Optional.of(studentInDb);

        } else {
            return Optional.empty();
        }
    }

    public List<Exam> getExamsOfStudent(Integer id) {
        List<Exam> examList = new ArrayList<>();
        Student student = classes.stream().map(c -> c.getStudents().stream().filter(s -> s.getStudentId() == id).findFirst()).collect(Collectors.toList()).get(0).get();

        examList = student.getExams();
        return examList;

    }

    public Exam removeExam(Long studentId, Long examId) {
        Student student = classes.stream().map(c -> c.getStudents().stream().filter(s -> s.getStudentId() == studentId).findFirst()).collect(Collectors.toList()).get(0).get();
        Exam exam = student.getExams().stream().filter(e -> e.getExamId() == examId).findFirst().get();
        student.getExams().remove(exam);
        return exam;
    }

    public Optional<Exam> addExam(Exam exam, Long studentId) {
        System.out.println(exam.getExamId());
        if (exam.getExamId() == 0) {
            System.out.println("What the fuck");
//      Long maxId = customers.stream().max(Comparator.comparing(Customer::getId)).get().getId();
            AtomicInteger integer = new AtomicInteger(0);
            classes.forEach(c -> {
                c.getStudents().forEach(s -> {
                    s.getExams().forEach(e -> {
                        if (e.getExamId()>integer.get()) integer.set((int) e.getExamId());
                    });
                });
            });
            exam.setExamId(integer.get() + 1);
            Student student = classes.stream().map(c -> c.getStudents().stream().filter(s -> s.getStudentId() == studentId).findFirst()).collect(Collectors.toList()).get(0).get();
            exam.setStudent(student);
            student.getExams().add(exam);
        } else {
            System.out.println("What the fuck 2");

            Student student = classes.stream().map(c -> c.getStudents().stream().filter(s -> s.getStudentId() == studentId).findFirst()).collect(Collectors.toList()).get(0).get();
            System.out.println(student);
            exam.setStudent(student);
            if (!student.getExams().contains(exam)) {
                student.getExams().add(exam);
            } else {
                return Optional.empty();
            }
        }
        return Optional.of(exam);
    }

}
