package com.phuc.tutoring_center.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student")
public class Student {
    @Id
    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "process_learning_id")
    private String processLearningId;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "current_school")
    private String currentSchool;
}
