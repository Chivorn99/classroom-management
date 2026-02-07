package com.example.classroom_management.controller;

import com.example.classroom_management.dto.StudentRegistrationDto;
import com.example.classroom_management.dto.StudentResponseDto;
import com.example.classroom_management.mapper.StudentMapper;
import com.example.classroom_management.model.Student;
import com.example.classroom_management.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;

    @PostMapping
    public ResponseEntity<StudentResponseDto> registerStudent(@RequestBody StudentRegistrationDto registrationDto) {
        Student createdStudent = studentService.registerStudent(registrationDto);
        return ResponseEntity.ok(studentMapper.toDto(createdStudent));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        // Convert to DTO before returning
        return ResponseEntity.ok(studentMapper.toDto(student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @Valid @RequestBody Student student) {
        Student existingStudent = studentService.getStudentById(id);
        if (existingStudent == null) {
            return ResponseEntity.notFound().build();
        }
        student.setId(id);
        Student updatedStudent = studentService.saveStudent(student);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{studentId}/department/{departmentId}")
//    public ResponseEntity<Student> assignDepartment(@PathVariable Long studentId, @PathVariable Long departmentId) {
//        Student student = studentService.assignDepartment(studentId, departmentId);
//        return ResponseEntity.ok(student);
//    }
    public ResponseEntity<StudentResponseDto> assignDepartment(
            @PathVariable Long studentId,
            @PathVariable Long departmentId
    ) {
        Student updatedStudent = studentService.assignDepartment(studentId, departmentId);
        return ResponseEntity.ok(studentMapper.toDto(updatedStudent));
    }
}