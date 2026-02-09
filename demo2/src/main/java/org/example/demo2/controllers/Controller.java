package org.example.demo2.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.demo2.Services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.example.demo2.entities.Student;
import org.example.demo2.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.example.demo2.repositories.StudentRepository;
import java.util.List;

@Slf4j
@RestController

@RequestMapping("/students")
public class Controller {
    @Autowired
    private StudentService studentService;


    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public Controller(StudentRepository studentRepository,
                      PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ADDITION API
    @GetMapping("/add")
    public int add(@RequestParam int a, @RequestParam int b) {
        log.info("Add API called with values {} and {}", a, b);
        return a + b;
    }

    // MULTIPLICATION API
    @GetMapping("/mul")
    public int mul(@RequestParam int a, @RequestParam int b) {
        log.info("Multiply API called with values {} and {}", a, b);
        return a * b;
    }

    // GET ALL STUDENTS
    @GetMapping
    public List<Student> getAllStudents() {
        log.info("Fetching all students");
        return studentRepository.findAll();
    }

    // CREATE STUDENT
    @PostMapping
    public Student createStudent(@RequestBody Student student) {

        log.info("Creating student with email {}", student.getEmail());

        student.setPassword(passwordEncoder.encode(student.getPassword()));

        Student savedStudent = studentService.saveStudent(student);


        log.info("Student created successfully with id {}", savedStudent.getId());

        return savedStudent;
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {

        log.info("Fetching student with id {}", id);

        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Student not found with id {}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });
    }

    // UPDATE
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable long id, @RequestBody Student user) {

        log.info("Updating student with id {}", id);

        Student userdata = studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Student not found for update with id {}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });

        userdata.setEmail(user.getEmail());
        userdata.setName(user.getName());

        Student updatedStudent = studentService.updateStudent(id, user);


        log.info("Student updated successfully with id {}", id);

        return updatedStudent;
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteStudent(@PathVariable long id) {

        log.info("Deleting student with id {}", id);

        Student userdata = studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Student not found for delete with id {}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });

        studentRepository.delete(userdata);

        log.info("Student deleted successfully with id {}", id);

        return ResponseEntity.ok().build();
    }
}
