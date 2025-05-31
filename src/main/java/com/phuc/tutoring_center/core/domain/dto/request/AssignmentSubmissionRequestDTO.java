package com.phuc.tutoring_center.core.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class AssignmentSubmissionRequestDTO {
    @NotBlank(message = "Assignment ID is required")
    private String assignmentId;

    @NotBlank(message = "Student ID is required")
    private String studentId;

    private List<MultipartFile> files;
} 