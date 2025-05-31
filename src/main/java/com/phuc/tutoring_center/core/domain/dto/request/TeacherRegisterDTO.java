package com.phuc.tutoring_center.core.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRegisterDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Date of birth is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotEmpty(message = "At least one subject is required")
    private Set<String> subjectIds;
}
