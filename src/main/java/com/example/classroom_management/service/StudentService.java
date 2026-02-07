package com.example.classroom_management.service;

import com.example.classroom_management.dto.StudentRegistrationDto;
import com.example.classroom_management.dto.StudentResponseDto;
import com.example.classroom_management.mapper.StudentMapper;
import com.example.classroom_management.model.AppUser;
import com.example.classroom_management.model.Department;
import com.example.classroom_management.model.Student;
import com.example.classroom_management.repository.AppUserRepository;
import com.example.classroom_management.repository.DepartmentRepository;
import com.example.classroom_management.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final DepartmentRepository departmentRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Student registerStudent(StudentRegistrationDto dto) {
        // Validation
        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email " + dto.getEmail() + " is already taken");
        }

        // Create the Login Account
        AppUser newUser = new AppUser();
        newUser.setUsername(dto.getEmail());
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        newUser.setRole("USER");
        appUserRepository.save(newUser);

        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setGpa(dto.getGpa());
        student.setAddress(dto.getAddress());
        student.setEnrollmentDate(LocalDate.now());

        if (dto.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            student.setDepartment(dept);
        }
        return studentRepository.save(student);
    }

    // Create
    public Student saveStudent(Student student) {
        if (student.getId() == null && studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email " + student.getEmail() + " is already taken");
        }
        return studentRepository.save(student);
    }

    // Read (All)
    public List<StudentResponseDto> getAllStudents() {
        List<Student> students = studentRepository.findAll();

        return students.stream()
                .map(studentMapper::toDto)
                .toList();
    }

    // Read (One)
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    // Delete
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    //Assign Department
    public Student assignDepartment(Long studentId, Long departmentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found!"));
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found!"));

        student.setDepartment(department);
        return studentRepository.save(student);
    }
}