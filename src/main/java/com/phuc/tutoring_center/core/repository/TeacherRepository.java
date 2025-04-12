package com.phuc.tutoring_center.core.repository;

import com.phuc.tutoring_center.core.domain.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {
}