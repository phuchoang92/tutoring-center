package com.phuc.tutoring_center.core.repository;

import com.phuc.tutoring_center.core.domain.entity.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, UUID> {
    List<AssignmentSubmission> findByAssignmentId(UUID assignmentId);
    List<AssignmentSubmission> findByStudent_StudentId(String studentId);
    Optional<AssignmentSubmission> findByAssignmentIdAndStudent_StudentId(UUID assignmentId, String studentId);
} 