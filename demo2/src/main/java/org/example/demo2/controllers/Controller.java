package org.example.demo2.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.demo2.model.StudentDTO;
import org.example.demo2.services.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/students")
public class Controller {

    private final StudentService studentService;

    public Controller(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        log.info("Fetching all students");
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(
            @Valid @RequestBody StudentDTO studentDTO) {

        log.info("Creating student with email {}", studentDTO.getEmail());

        StudentDTO savedStudent = studentService.createStudent(studentDTO);

        log.info("Student created successfully");

        return ResponseEntity.status(201).body(savedStudent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {

        log.info("Fetching student with id {}", id);

        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentDTO studentDTO) {

        log.info("Updating student with id {}", id);

        return ResponseEntity.ok(studentService.updateStudent(id, studentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {

        log.info("Deleting student with id {}", id);

        studentService.deleteStudent(id);

        return ResponseEntity.noContent().build();
    }
}
