package com.example.classroom_management.dto;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentDto {
    private String name;
    private List<StudentResponseDto> students;
}
