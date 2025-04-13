package com.phuc.tutoring_center.core.repository;

import com.phuc.tutoring_center.core.domain.entity.ClassSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, UUID> {
}