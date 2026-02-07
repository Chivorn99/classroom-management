package com.example.classroom_management.repository;

import com.example.classroom_management.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // MAGIC: By extending JpaRepository, you instantly get:
    // .save(), .findAll(), .findById(), .deleteById()
    // Spring writes the SQL for you!
    // Spring sees "existsBy" and "Email" and automatically writes:
    // SELECT COUNT(*) > 0 FROM student WHERE email = ?
    boolean existsByEmail(String email);
}