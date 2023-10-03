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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.annotation.Repeatable;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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

        List<Student> students = mockDB.getAllStudents();
        if (!(order.equals(""))) {
            Comparator<String> customComparator = new Comparator<String>() {
                @Override
                public int compare(Student s1, Student s2) {

                    return Integer.compare(s1.length(), s2.length());
                }
            };
        }

        System.out.println(str);
        if (str.equals("")) return ResponseEntity.ok(mockDB.getAllStudents());
        return ResponseEntity.ok(mockDB.getAllStudents().stream().filter(s -> s.getClasslist().getClassname().equals(str)).collect(Collectors.toList()));
    }


}
