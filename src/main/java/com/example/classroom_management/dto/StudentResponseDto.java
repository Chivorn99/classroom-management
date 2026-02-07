package com.example.classroom_management.dto;

import com.example.classroom_management.model.Address;
import jakarta.persistence.Embedded;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentResponseDto {
    private String name;
    private String email;
    private Double gpa;
    private String departmentName;
    private LocalDate enrollmentDate;
//    private DepartmentDto department;
    private Address address;
}