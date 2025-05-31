package com.phuc.tutoring_center.core.domain.entity;

import com.phuc.tutoring_center.core.domain.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attendance")
public class Attendance {
    @Id
    @Column(name = "attendance_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Class clazz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public boolean isCountedAsPresent() {
        return status.isCountedAsPresent();
    }
} 