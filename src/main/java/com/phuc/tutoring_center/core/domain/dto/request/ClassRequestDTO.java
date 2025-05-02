package com.phuc.tutoring_center.core.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassRequestDTO {
    private String className;

    private String schedule;

    private String description;

    private String maxStudents;

    private String teacherId;

    private String price;

    private String startDate;

    private String endDate;
}
