package at.kaindorf.examdb.backend;

import at.kaindorf.examdb.ExamMockDB;
import at.kaindorf.examdb.beans.ClassList;
import at.kaindorf.examdb.beans.Exam;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Project: Exa_01_Spring_Intro
 * Created by: SF
 * Date: 20.09.2023
 * Time: 08:17
 */
@RestController
@RequestMapping("/exams")
@Slf4j
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ExamResource {

    private final ExamMockDB mockDB;

    @GetMapping("")
    public ResponseEntity<Iterable<Exam>> getAllExams(@RequestParam(name = "studentId", required = true)Integer id) {
        System.out.println(id);
        return ResponseEntity.ok(mockDB.getExamsOfStudent(id));

    }

    @DeleteMapping("/{studentId}/{examId}")
    public ResponseEntity<Exam> getAllExams(@PathVariable Long studentId, @PathVariable Long examId) {
        return ResponseEntity.ok(mockDB.removeExam(studentId, examId));

    }

    @PostMapping("/{studentId}")
    public ResponseEntity<Exam> addExam(@RequestBody Exam exam, @PathVariable Long studentId) {
        Optional<Exam> examOptional = mockDB.addExam(exam, studentId);
        if (examOptional.isPresent()) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{studentId}")
                    .buildAndExpand(examOptional.get().getExamId())
                    .toUri();
            return ResponseEntity.created(location).build();
            //return ResponseEntity.status(HttpStatus.CREATED).body(customerOptional.get());
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }


}
