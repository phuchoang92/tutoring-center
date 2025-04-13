package com.phuc.tutoring_center.core.repository;

import com.phuc.tutoring_center.core.domain.entity.Register;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterRepository extends JpaRepository<Register, String> {
}