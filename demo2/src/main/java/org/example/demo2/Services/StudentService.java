package org.example.demo2.Services;

import org.example.demo2.entities.Student;
import org.example.demo2.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // CREATE
    public Student saveStudent(Student student){

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        student.setCreatedBy(username);
        student.setModifiedBy(username);

        return studentRepository.save(student);
    }

    // UPDATE
    public Student updateStudent(Long id, Student student){

        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        existing.setName(student.getName());
        existing.setEmail(student.getEmail());
        existing.setModifiedBy(username);

        return studentRepository.save(existing);
    }
}
