package com.phuc.tutoring_center.core.service.impl;

import com.phuc.tutoring_center.core.domain.dto.request.LoginRequest;
import com.phuc.tutoring_center.core.domain.dto.response.JwtResponse;
import com.phuc.tutoring_center.core.domain.entity.Student;
import com.phuc.tutoring_center.core.repository.StudentRepository;
import com.phuc.tutoring_center.core.service.AuthService;
import com.phuc.tutoring_center.core.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final StudentRepository studentRepository;


    public AuthServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public JwtResponse signIn(LoginRequest loginRequest) {
        Student student = studentRepository.findStudentByEmailOrPhoneNumber(loginRequest.getEmail(), loginRequest.getEmail())
                .orElse(null);
        if (Objects.isNull(student)){
            throw new RuntimeException("No account found");
        }
        JwtResponse jwtResponse = JwtResponse.builder()
                .email(student.getEmail())
                .roles(Collections.singletonList("Student"))
                .build();
        return null;
    }
}
