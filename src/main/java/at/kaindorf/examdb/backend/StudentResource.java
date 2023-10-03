package at.kaindorf.examdb.backend;

import at.kaindorf.examdb.ExamMockDB;
import at.kaindorf.examdb.beans.Student;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.annotation.Repeatable;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Project: Exa_01_Spring_Intro
 * Created by: SF
 * Date: 20.09.2023
 * Time: 08:17
 */
@RestController
@RequestMapping("/students")
@Slf4j
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class StudentResource {

    private final ExamMockDB mockDB;

    @GetMapping("/all")
    public ResponseEntity<Iterable<Student>> getAllStudents(@RequestParam(name = "class", required = false, defaultValue = "")String str, @RequestParam(name = "orderBy", required = false, defaultValue = "")String order) {
        List<String> criteria = new ArrayList<>();
        criteria = List.of("firstname", "lastname", "studentid");
        List<Student> students = mockDB.getAllStudents();
        if (!(order.equals("")) && criteria.contains(order)) {
            System.out.println("ping");
            if (order.equals(criteria.get(0))) {
                System.out.println("Am i in here");
                Collections.sort(students, Comparator.comparing(Student::getFirstname));
            }
            else if (order.equals(criteria.get(1))) Collections.sort(students, Comparator.comparing(Student::getLastname));
            else if (order.equals(criteria.get(2))) Collections.sort(students, Comparator.comparing(Student::getStudentId));


        }

        System.out.println(str);
        if (str.equals("")) return ResponseEntity.ok(students);
        return ResponseEntity.ok(students.stream().filter(s -> s.getClasslist().getClassname().equals(str)).collect(Collectors.toList()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Student> patchCustomer(@PathVariable Long id, @RequestBody Student student) {
        Optional<Student> studentOptional = mockDB.patchStudent(id, student);
        if (studentOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(studentOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }




}
