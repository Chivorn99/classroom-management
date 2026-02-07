package com.example.classroom_management.mapper;

import com.example.classroom_management.dto.StudentResponseDto;
import com.example.classroom_management.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "address", target = "address")
    StudentResponseDto toDto(Student student);
    Student toEntity(StudentResponseDto dto);
}