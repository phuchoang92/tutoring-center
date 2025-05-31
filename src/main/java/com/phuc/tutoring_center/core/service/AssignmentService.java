package com.phuc.tutoring_center.core.service;

import com.phuc.tutoring_center.core.domain.dto.request.AssignmentRequestDTO;
import com.phuc.tutoring_center.core.domain.dto.request.AssignmentSubmissionRequestDTO;
import com.phuc.tutoring_center.core.domain.entity.Assignment;
import com.phuc.tutoring_center.core.domain.entity.AssignmentSubmission;

import java.util.List;

public interface AssignmentService {
    Assignment createAssignment(AssignmentRequestDTO request);
    Assignment updateAssignment(String id, AssignmentRequestDTO request);
    void deleteAssignment(String id);
    Assignment getAssignment(String id);
    List<Assignment> getAssignmentsByClass(String classId);
    List<Assignment> getAssignmentsByTeacher(String teacherId);
    List<Assignment> getAssignmentsByStudent(String studentId);
    
    // Submission methods
    AssignmentSubmission submitAssignment(AssignmentSubmissionRequestDTO request);
    AssignmentSubmission updateSubmission(String submissionId, AssignmentSubmissionRequestDTO request);
    void deleteSubmission(String submissionId);
    AssignmentSubmission getSubmission(String submissionId);
    List<AssignmentSubmission> getSubmissionsByAssignment(String assignmentId);
    List<AssignmentSubmission> getSubmissionsByStudent(String studentId);
    AssignmentSubmission gradeSubmission(String submissionId, Double grade, String feedback);
} 