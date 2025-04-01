package com.phuc.tutoring_center.core.repository;

import com.phuc.tutoring_center.core.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {
    Optional<Student> findByPhoneNumber(String phoneNumber);
    Optional<Student> findStudentByEmailOrPhoneNumber(String email, String phoneNumber);
}
