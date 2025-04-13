package com.phuc.tutoring_center.core.repository;

import com.phuc.tutoring_center.core.domain.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, String> {
}