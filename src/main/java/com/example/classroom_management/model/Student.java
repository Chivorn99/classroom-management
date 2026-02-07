package com.example.classroom_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;
    private LocalDate enrollmentDate;

    @NotNull(message = "GPA is mandatory")
    private Double gpa;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StudentEnum status = StudentEnum.ACTIVE;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = StudentEnum.ACTIVE;
        }
        if (enrollmentDate == null) {
            enrollmentDate = LocalDate.now();
        }
    }

    @Embedded
    private Address address;

}