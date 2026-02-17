package org.example.demo2.repositories;

import org.example.demo2.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
   Optional<Student> findByUsername(String username);
   @Query("select s.id from Student s where s.username=:username")
   Optional<Long> findIdByUsername(@Param("username") String username);

   boolean existsByUsername(String username);
   boolean existsByEmail(String email);
}