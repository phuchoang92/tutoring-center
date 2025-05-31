package com.phuc.tutoring_center.core.domain.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ClassRequestDTO {
    @NotBlank(message = "Class name is required")
    private String className;

    @NotBlank(message = "Teacher ID is required")
    private String teacherId;

    @NotBlank(message = "Start date is required")
    private String startDate;

    @NotBlank(message = "End date is required")
    private String endDate;

    @NotBlank(message = "Price is required")
    private String price;

    @NotBlank(message = "Maximum students is required")
    private String maxStudents;

    @NotEmpty(message = "At least one schedule is required")
    @Valid
    private List<ScheduleRequestDTO> schedules;
}
