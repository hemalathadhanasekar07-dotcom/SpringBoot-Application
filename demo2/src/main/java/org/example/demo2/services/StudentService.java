package org.example.demo2.services;

import lombok.extern.slf4j.Slf4j;
import org.example.demo2.entities.Student;
import org.example.demo2.exceptions.UnauthorizedActionException;
import org.example.demo2.model.StudentDTO;
import org.example.demo2.repositories.StudentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;

    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository,
                          PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // CREATE
    public Student createStudent(StudentDTO dto) {

        log.info("Creating new student");

        Student student = new Student();
        String fullName= dto.getFirstName()+" "+dto.getLastName();
        student.setName(fullName);
        student.setEmail(dto.getEmail());
        student.setUsername(dto.getUsername());
        student.setPassword(passwordEncoder.encode(dto.getPassword()));
        student.setRole(dto.getRole());

        Student savedStudent = studentRepository.save(student);

        savedStudent.setCreatedBy(savedStudent.getId());
        savedStudent.setModifiedBy(savedStudent.getId());

        return studentRepository.save(savedStudent);
    }

    // UPDATE
    public Student updateStudent(Long id, StudentDTO dto) {

        log.info("Updating student with ID: {}", id);

        Student existing = studentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Student not found with ID: " + id)
                );

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        Long loggedInUserId =
                studentRepository.findIdByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException("Logged-in user not found")
                        );


        if (!loggedInUserId.equals(existing.getId())) {
            log.warn("Unauthorized update attempt by user ID: {}", loggedInUserId);

            throw new UnauthorizedActionException(
                    "Only the logged-in user can update their own profile."
            );
        }

        String fullName= dto.getFirstName()+" "+dto.getLastName();
        existing.setName(fullName);
        existing.setEmail(dto.getEmail());
        existing.setModifiedBy(loggedInUserId);

        log.info("Student updated successfully with ID: {}", id);

        return studentRepository.save(existing);
    }
    public List<Student> getAllStudents() {

        log.info("Fetching all students");

        return studentRepository.findAll();
    }
    public Student getStudentById(Long id) {

        log.info("Fetching student with ID: {}", id);

        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Student not found with ID: " + id)
                );
    }
    public void deleteStudent(Long id) {

        log.info("Deleting student with ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Student not found with ID: " + id)
                );

        studentRepository.delete(student);

        log.info("Student deleted successfully with ID: {}", id);
    }






}

