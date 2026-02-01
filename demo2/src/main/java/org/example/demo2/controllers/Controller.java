package org.example.demo2.controllers;
import org.springframework.http.HttpStatus;
import org.example.demo2.entities.Student;
import org.example.demo2.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.demo2.repositories.StudentRepository;
import java.util.List;

@RestController
@RequestMapping("/students")
public class Controller {

    private final StudentRepository studentRepository;

    public Controller(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/add")
    public int add(@RequestParam int a, @RequestParam int b) {
        return a + b;
    }

    @GetMapping("/mul")
    public int mul(@RequestParam int a, @RequestParam int b) {
        return a * b;
    }

    // GET ALL STUDENTS
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // CREATE STUDENT
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable long id, @RequestBody Student user) {
        Student userdata = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userdata.setEmail(user.getEmail());
        userdata.setName(user.getName());

        return studentRepository.save(userdata);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteStudent(@PathVariable long id) {
        Student userdata = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        studentRepository.delete(userdata);
        return ResponseEntity.ok().build();
    }
}
