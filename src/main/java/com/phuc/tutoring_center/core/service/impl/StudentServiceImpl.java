package com.phuc.tutoring_center.core.service.impl;

import com.phuc.tutoring_center.core.domain.dto.request.StudentRegisterDTO;
import com.phuc.tutoring_center.core.domain.entity.Student;
import com.phuc.tutoring_center.core.domain.entity.User;
import com.phuc.tutoring_center.core.exception.BusinessException;
import com.phuc.tutoring_center.core.repository.StudentRepository;
import com.phuc.tutoring_center.core.repository.UserRepository;
import com.phuc.tutoring_center.core.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Student registerStudent(@Valid StudentRegisterDTO registerDTO) {
        log.info("Starting student registration for email: {}", registerDTO.getEmail());
        
        validateRegisterRequest(registerDTO);
        
        // Check if user already exists
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            log.warn("Registration failed: Email {} already exists", registerDTO.getEmail());
            throw new BusinessException("Email already exists", 400, "EMAIL_EXISTS");
        }

        // Create user account
        User user = User.builder()
                .id(UUID.randomUUID())
                .phoneNumber(registerDTO.getPhoneNumber())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .role(User.UserRole.STUDENT)
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .createdAt(LocalDateTime.now())
                .build();
        
        userRepository.save(user);
        log.debug("Created user account for student: {}", user.getEmail());

        // Create student profile
        Student student = Student.builder()
                .studentId(UUID.randomUUID().toString())
                .user(user)
                .currentSchool(registerDTO.getCurrentSchool())
                .createdAt(LocalDateTime.now())
                .build();
        
        studentRepository.save(student);
        log.info("Successfully registered student: {}", student.getStudentId());
        
        return student;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> listStudents() {
        log.debug("Fetching all students");
        List<Student> students = studentRepository.findAll();
        log.info("Retrieved {} students", students.size());
        return students;
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudentInformation(String id) {
        log.debug("Fetching student information for ID: {}", id);
        
        if (!StringUtils.hasText(id)) {
            throw new BusinessException("Student ID is required", 400, "INVALID_ID");
        }

        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Student not found with ID: {}", id);
                    return new BusinessException("Student not found", 404, "STUDENT_NOT_FOUND");
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> listStudentsByClass() {
        log.debug("Fetching students grouped by class");
        // TODO: Implement this method when Class entity and relationships are properly set up
        throw new BusinessException("Method not implemented", 501, "NOT_IMPLEMENTED");
    }

    private void validateRegisterRequest(StudentRegisterDTO registerDTO) {
        log.debug("Validating registration request for email: {}", registerDTO.getEmail());

        if (!StringUtils.hasText(registerDTO.getEmail())) {
            throw new BusinessException("Email is required", 400, "INVALID_EMAIL");
        }

        if (!StringUtils.hasText(registerDTO.getPassword())) {
            throw new BusinessException("Password is required", 400, "INVALID_PASSWORD");
        }

        if (!StringUtils.hasText(registerDTO.getPhoneNumber())) {
            throw new BusinessException("Phone number is required", 400, "INVALID_PHONE");
        }

        if (!StringUtils.hasText(registerDTO.getCurrentSchool())) {
            throw new BusinessException("Current school is required", 400, "INVALID_SCHOOL");
        }
    }
}
