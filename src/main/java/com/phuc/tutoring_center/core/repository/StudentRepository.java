package com.phuc.tutoring_center.core.repository;

import com.phuc.tutoring_center.core.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {
    @NativeQuery(value = "SELECT * FROM students s INNER JOIN users u ON s.user_id = u.user_id WHERE u.phone_number = :phoneNumber")
    Optional<Student> findByPhoneNumber(String phoneNumber);

    @NativeQuery(value = "SELECT * " +
            "FROM students s INNER JOIN users u ON s.user_id = u.user_id " +
            "WHERE u.phone_number = :phoneNumber OR u.email = :email")
    Optional<Student> findStudentByEmailOrPhoneNumber(String email, String phoneNumber);
}
