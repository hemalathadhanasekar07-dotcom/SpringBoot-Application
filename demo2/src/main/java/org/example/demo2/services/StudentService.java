package org.example.demo2.services;

import lombok.extern.slf4j.Slf4j;
import org.example.demo2.entities.Student;
import org.example.demo2.exceptions.DuplicateResourceException;
import org.example.demo2.exceptions.ResourceNotFoundException;
import org.example.demo2.exceptions.UnauthorizedActionException;
import org.example.demo2.model.StudentDTO;
import org.example.demo2.repositories.StudentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository,
                          PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }



    private Student dtoToEntity(StudentDTO dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setUsername(dto.getUsername());
        student.setName(dto.getFirstName() + " " + dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setRole(dto.getRole());
        student.setPassword(dto.getPassword()); // encoded later
        return student;
    }

    private StudentDTO entityToDto(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setUsername(student.getUsername());

        if (student.getName() != null && student.getName().contains(" ")) {
            String[] parts = student.getName().split(" ", 2);
            dto.setFirstName(parts[0]);
            dto.setLastName(parts[1]);
        } else {
            dto.setFirstName(student.getName());
            dto.setLastName("");
        }

        dto.setEmail(student.getEmail());
        dto.setRole(student.getRole());
        dto.setCreatedBy(student.getCreatedBy());
        dto.setModifiedBy(student.getModifiedBy());
        dto.setPassword(student.getPassword());
        return dto;
    }



    public StudentDTO createStudent(StudentDTO dto) {

        log.info("Creating student");

        if (studentRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        Student student = dtoToEntity(dto);
        student.setPassword(passwordEncoder.encode(dto.getPassword()));
        student.setRole(dto.getRole().toUpperCase());

        Student saved = studentRepository.save(student);
        saved.setCreatedBy(saved.getId());
        saved.setModifiedBy(saved.getId());

        return entityToDto(studentRepository.save(saved));
    }



    public StudentDTO updateStudent(Long id, StudentDTO dto) {

        Student existing = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found with id " + id));

        Student currentStudent = getLoggedInStudent();

        if (!existing.getCreatedBy().equals(currentStudent.getId())) {
            log.warn("Unauthorized update attempt by {}", currentStudent.getId());
            throw new UnauthorizedActionException(
                    "Only the logged-in student can update their own profile");
        }

        existing.setName(dto.getFirstName() + " " + dto.getLastName());
        existing.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        existing.setModifiedBy(currentStudent.getId());

        return entityToDto(studentRepository.save(existing));
    }



    public List<StudentDTO> getAllStudents() {

        Student currentStudent = getLoggedInStudent();

        if (!"ADMIN".equalsIgnoreCase(currentStudent.getRole())) {
            throw new UnauthorizedActionException("Only admin can view all students");
        }

        return studentRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public StudentDTO getStudentById(Long id) {

        Student target = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found with id " + id));

        Student currentStudent = getLoggedInStudent();

        if (!target.getCreatedBy().equals(currentStudent.getId())
                && !"ADMIN".equalsIgnoreCase(currentStudent.getRole())) {
            throw new UnauthorizedActionException("You are not allowed to view this profile");
        }

        return entityToDto(target);
    }



    public void deleteStudent(Long id) {

        Student delete = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found with id " + id));

        Student currentStudent = getLoggedInStudent();

        if (!delete.getCreatedBy().equals(currentStudent.getId())) {
            throw new UnauthorizedActionException(
                    "Only the created student can delete their profile");
        }

        studentRepository.deleteById(id);
        log.info("Student deleted with id {}", id);
    }



    private Student getLoggedInStudent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return studentRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Logged-in student not found"));
    }
}
