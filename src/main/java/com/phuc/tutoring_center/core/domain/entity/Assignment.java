package com.phuc.tutoring_center.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@Table(name = "assignment")
public class Assignment {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private Class clazz;

    @Column
    private String name;

    @Column(name = "subject")
    private String subject;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "closed_time", nullable = false)
    private LocalDateTime closedTime;

    @Column(name = "due_time")
    private LocalDateTime dueTime;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
