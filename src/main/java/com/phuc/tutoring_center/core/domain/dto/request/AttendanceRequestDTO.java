package com.phuc.tutoring_center.core.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.phuc.tutoring_center.core.domain.enums.AttendanceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttendanceRequestDTO {
    @NotBlank(message = "Class ID is required")
    private String classId;

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotNull(message = "Date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime date;

    @NotNull(message = "Status is required")
    private AttendanceStatus status;

    private String note;
} 