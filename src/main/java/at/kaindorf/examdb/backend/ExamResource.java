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
@RequestMapping("/exam")
@Slf4j
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ExamResource {

    private final ExamMockDB mockDB;


    @DeleteMapping("/{studentId}/{examId}")
    public ResponseEntity<Exam> getAllExams(@PathVariable Long studentId, @PathVariable Long examId) {

        try {
            return ResponseEntity.ok(mockDB.removeExam(studentId, examId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


    }

    @PostMapping("/{studentId}")
    public ResponseEntity<Exam> addExam(@RequestBody Exam exam, @PathVariable Long studentId) {
        if (!((mockDB.getExamsOfStudent(Math.toIntExact(studentId)).stream().filter(e -> e.getDateOfExam().equals(exam.getDateOfExam()))).collect(Collectors.toList()).size() >= 1)) {
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
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }


}
