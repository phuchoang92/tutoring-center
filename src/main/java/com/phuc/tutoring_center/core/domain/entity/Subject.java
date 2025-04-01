package com.phuc.tutoring_center.core.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Subject {
    @Id
    @Column(name = "subject_id")
    private String subjectId;

    private String subjectName;
    private String description;
}
