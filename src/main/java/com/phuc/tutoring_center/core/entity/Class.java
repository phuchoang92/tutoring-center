package com.phuc.tutoring_center.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Class {
    @Id
    @Column(name = "class_id")
    private String classId;

    private String subjectId;

    private String className;

    private String teacherId;
    private Integer gradeLevel;

    private Integer maxStudent;

    private String schedule;

    private String createdAt;
}
