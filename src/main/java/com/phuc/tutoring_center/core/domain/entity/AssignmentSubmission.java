package com.phuc.tutoring_center.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assignment_submission")
public class AssignmentSubmission {
    @Id
    @Column(name = "submission_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "submission_time", nullable = false)
    private LocalDateTime submissionTime;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;

    @Column(name = "grade")
    private Double grade;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SubmissionFile> files = new ArrayList<>();

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum SubmissionStatus {
        SUBMITTED,
        LATE,
        GRADED,
        RETURNED
    }
}