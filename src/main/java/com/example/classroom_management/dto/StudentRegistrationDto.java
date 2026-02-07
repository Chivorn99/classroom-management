package com.example.classroom_management.dto;

import lombok.Data;
import java.time.LocalDate;
import com.example.classroom_management.model.Address;

@Data
public class StudentRegistrationDto {
    private String name;
    private String email;
    private Double gpa;
    private Long departmentId;
    private Address address;

    // Security Fields
    private String password;

}