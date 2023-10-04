package at.kaindorf.examdb.backend;

import at.kaindorf.examdb.ExamMockDB;
import at.kaindorf.examdb.beans.Exam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
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
public class ExamsResource {

    private final ExamMockDB mockDB;

    @GetMapping("")
    public ResponseEntity<Iterable<Exam>> getAllExams(@RequestParam(name = "studentId", required = true)Integer id) {
        if (id==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(mockDB.getExamsOfStudent(id));

    }


}
