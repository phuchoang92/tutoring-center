package com.phuc.tutoring_center.core.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class StudentRegisterDTO {
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;

    private String address;

    private String phoneNumber;

    private String email;

    private String password;

    private String currentSchool;
}
