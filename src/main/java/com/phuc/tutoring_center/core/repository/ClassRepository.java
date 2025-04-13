package com.phuc.tutoring_center.core.repository;

import com.phuc.tutoring_center.core.domain.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<Class, String> {
}