package com.example.classroom_management.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Address {

    private String street;
    private String city;
    private String khan;
    private String zipCode;
}
