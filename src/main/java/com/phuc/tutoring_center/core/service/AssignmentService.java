package com.phuc.tutoring_center.core.service;

import com.phuc.tutoring_center.core.domain.dto.request.AssignmentRequestDTO;
import com.phuc.tutoring_center.core.domain.entity.Assignment;

import java.util.List;

public interface AssignmentService {
    Assignment createAssignment(AssignmentRequestDTO request);
    Assignment updateAssignment(String id, AssignmentRequestDTO request);
    void deleteAssignment(String id);
    Assignment getAssignment(String id);
    List<Assignment> getAssignmentsByClass(String classId);
    List<Assignment> getAssignmentsByTeacher(String teacherId);
    List<Assignment> getAssignmentsByStudent(String studentId);
} 