package org.example.demo2.controllers;

import org.example.demo2.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.demo2.repositories.StudentRepository;

import java.util.List;

@RestController
public class Controller {
    private final StudentRepository studentRepository;
    public Controller(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/add_")
    public int add(
            @RequestParam int a,
            @RequestParam int b) {
        return a + b;
    }

    @GetMapping("/mul_")
    public int mul(
            @RequestParam int a,
            @RequestParam int b){
        return a * b ;
    }

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    @PostMapping("/createstudent")
    public Student createuser(@RequestBody Student user){
         return studentRepository.save(user);

    }
    @GetMapping("Student/{id}")
    public Student getuserbyId() {


    }

}
