package com.phuc.tutoring_center.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRegisterDTO {
    private String name;

    private Integer age;

    private LocalDate dateOfBirth;

    private String address;

    private String phoneNumber;
}
