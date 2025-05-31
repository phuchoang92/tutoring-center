package com.phuc.tutoring_center.core.service.impl;

import com.phuc.tutoring_center.core.domain.dto.request.TeacherRegisterDTO;
import com.phuc.tutoring_center.core.domain.entity.Subject;
import com.phuc.tutoring_center.core.domain.entity.Teacher;
import com.phuc.tutoring_center.core.domain.entity.User;
import com.phuc.tutoring_center.core.exception.BusinessException;
import com.phuc.tutoring_center.core.repository.SubjectRepository;
import com.phuc.tutoring_center.core.repository.TeacherRepository;
import com.phuc.tutoring_center.core.repository.UserRepository;
import com.phuc.tutoring_center.core.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final PasswordEncoder passwordEncoder;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    @Transactional
    public Teacher registerTeacher(@Valid TeacherRegisterDTO registerDTO) {
        log.info("Starting teacher registration for email: {}", registerDTO.getEmail());
        
        validateRegisterRequest(registerDTO);
        
        // Check if user already exists
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            log.warn("Registration failed: Email {} already exists", registerDTO.getEmail());
            throw new BusinessException("Email already exists", 400, "EMAIL_EXISTS");
        }

        // Create user account
        User user = User.builder()
                .id(UUID.randomUUID())
                .name(registerDTO.getName())
                .dateOfBirth(registerDTO.getDateOfBirth())
                .address(registerDTO.getAddress())
                .phoneNumber(registerDTO.getPhoneNumber())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .role(User.UserRole.TEACHER)
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .createdAt(LocalDateTime.now())
                .build();
        
        userRepository.save(user);
        log.debug("Created user account for teacher: {}", user.getEmail());

        // Get subjects
        Set<Subject> subjects = registerDTO.getSubjectIds().stream()
                .map(subjectId -> subjectRepository.findById(subjectId)
                        .orElseThrow(() -> {
                            log.warn("Subject not found with ID: {}", subjectId);
                            return new BusinessException("Subject not found: " + subjectId, 404, "SUBJECT_NOT_FOUND");
                        }))
                .collect(Collectors.toSet());

        // Create teacher profile
        Teacher teacher = Teacher.builder()
                .teacherId(UUID.randomUUID())
                .user(user)
                .subjects(subjects)
                .createdAt(LocalDateTime.now())
                .build();
        
        teacherRepository.save(teacher);
        log.info("Successfully registered teacher: {}", teacher.getTeacherId());
        
        return teacher;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> listTeachers() {
        log.debug("Fetching all teachers");
        List<Teacher> teachers = teacherRepository.findAll();
        log.info("Retrieved {} teachers", teachers.size());
        return teachers;
    }

    @Override
    @Transactional(readOnly = true)
    public Teacher getTeacherInformation(String id) {
        log.debug("Fetching teacher information for ID: {}", id);
        
        if (!StringUtils.hasText(id)) {
            throw new BusinessException("Teacher ID is required", 400, "INVALID_ID");
        }

        return teacherRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> {
                    log.warn("Teacher not found with ID: {}", id);
                    return new BusinessException("Teacher not found", 404, "TEACHER_NOT_FOUND");
                });
    }

    @Override
    @Transactional
    public Teacher updateTeacher(String id, TeacherRegisterDTO updateDTO) {
        log.info("Updating teacher with ID: {}", id);
        
        Teacher teacher = getTeacherInformation(id);
        User user = teacher.getUser();
        
        // Update user information
        user.setName(updateDTO.getName());
        user.setDateOfBirth(updateDTO.getDateOfBirth());
        user.setAddress(updateDTO.getAddress());
        user.setPhoneNumber(updateDTO.getPhoneNumber());
        
        // Update subjects if provided
        if (updateDTO.getSubjectIds() != null && !updateDTO.getSubjectIds().isEmpty()) {
            Set<Subject> subjects = updateDTO.getSubjectIds().stream()
                    .map(subjectId -> subjectRepository.findById(subjectId)
                            .orElseThrow(() -> {
                                log.warn("Subject not found with ID: {}", subjectId);
                                return new BusinessException("Subject not found: " + subjectId, 404, "SUBJECT_NOT_FOUND");
                            }))
                    .collect(Collectors.toSet());
            teacher.setSubjects(subjects);
        }
        
        userRepository.save(user);
        teacherRepository.save(teacher);
        
        log.info("Successfully updated teacher: {}", id);
        return teacher;
    }

    @Override
    @Transactional
    public void deleteTeacher(String id) {
        log.info("Deleting teacher with ID: {}", id);
        
        Teacher teacher = getTeacherInformation(id);
        User user = teacher.getUser();
        
        teacherRepository.delete(teacher);
        userRepository.delete(user);
        
        log.info("Successfully deleted teacher: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Teacher> getTeachersBySubject(String subjectId) {
        log.debug("Fetching teachers for subject: {}", subjectId);
        
        if (!StringUtils.hasText(subjectId)) {
            throw new BusinessException("Subject ID is required", 400, "INVALID_SUBJECT_ID");
        }

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> {
                    log.warn("Subject not found with ID: {}", subjectId);
                    return new BusinessException("Subject not found", 404, "SUBJECT_NOT_FOUND");
                });

        return subject.getTeachers();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> getAvailableTeachers(String subjectId, String dayOfWeek, String startTime, String endTime) {
        log.debug("Finding available teachers for subject: {} on {} from {} to {}", 
                subjectId, dayOfWeek, startTime, endTime);
        
        // Validate inputs
        if (!StringUtils.hasText(subjectId) || !StringUtils.hasText(dayOfWeek) || 
            !StringUtils.hasText(startTime) || !StringUtils.hasText(endTime)) {
            throw new BusinessException("All parameters are required", 400, "INVALID_PARAMETERS");
        }

        try {
            LocalTime start = LocalTime.parse(startTime, TIME_FORMATTER);
            LocalTime end = LocalTime.parse(endTime, TIME_FORMATTER);
            
            if (start.isAfter(end)) {
                throw new BusinessException("Start time must be before end time", 400, "INVALID_TIME_RANGE");
            }
        } catch (Exception e) {
            throw new BusinessException("Invalid time format. Use HH:mm", 400, "INVALID_TIME_FORMAT");
        }

        // Get all teachers for the subject
        Set<Teacher> subjectTeachers = getTeachersBySubject(subjectId);
        
        // TODO: Implement schedule conflict checking
        // This would require additional repository methods to check teacher schedules
        // For now, return all teachers for the subject
        return new ArrayList<>(subjectTeachers);
    }

    private void validateRegisterRequest(TeacherRegisterDTO registerDTO) {
        if (!StringUtils.hasText(registerDTO.getName())) {
            throw new BusinessException("Name is required", 400, "INVALID_NAME");
        }

        if (registerDTO.getDateOfBirth() == null) {
            throw new BusinessException("Date of birth is required", 400, "INVALID_DATE_OF_BIRTH");
        }

        if (!StringUtils.hasText(registerDTO.getAddress())) {
            throw new BusinessException("Address is required", 400, "INVALID_ADDRESS");
        }

        if (!StringUtils.hasText(registerDTO.getPhoneNumber())) {
            throw new BusinessException("Phone number is required", 400, "INVALID_PHONE");
        }

        if (!StringUtils.hasText(registerDTO.getEmail())) {
            throw new BusinessException("Email is required", 400, "INVALID_EMAIL");
        }

        if (!StringUtils.hasText(registerDTO.getPassword())) {
            throw new BusinessException("Password is required", 400, "INVALID_PASSWORD");
        }

        if (registerDTO.getSubjectIds() == null || registerDTO.getSubjectIds().isEmpty()) {
            throw new BusinessException("At least one subject is required", 400, "INVALID_SUBJECTS");
        }
    }
} 