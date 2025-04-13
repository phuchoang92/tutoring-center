package com.phuc.tutoring_center.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Getter
@Setter
@ToString
@Builder
@Table(name = "students")
@AllArgsConstructor
@RequiredArgsConstructor
public class Student {
    @Id
    @Column(name = "student_id", nullable = false)
    private String studentId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "student")
    @ToString.Exclude
    private Set<Register> registers;

    @Column(name = "current_school")
    private String currentSchool;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
