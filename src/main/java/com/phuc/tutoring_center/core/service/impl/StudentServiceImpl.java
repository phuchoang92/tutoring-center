package com.phuc.tutoring_center.core.service.impl;

import com.phuc.tutoring_center.core.domain.dto.request.StudentRegisterDTO;
import com.phuc.tutoring_center.core.domain.entity.Student;
import com.phuc.tutoring_center.core.domain.entity.User;
import com.phuc.tutoring_center.core.repository.StudentRepository;
import com.phuc.tutoring_center.core.repository.UserRepository;
import com.phuc.tutoring_center.core.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public StudentServiceImpl(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Student registerStudent(StudentRegisterDTO registerDTO) {
        log.info("Start register student with request: {}", registerDTO);
        if (!validateRegisterRequest(registerDTO)){
            throw new RuntimeException("Invalid field");
        }
        User student = userRepository.findByEmail(registerDTO.getEmail())
                .orElse(null);
        if (!Objects.isNull(student)){
            throw new RuntimeException("This phone number has existed");
        }
        String passwordHashed = BCrypt.hashpw(registerDTO.getPassword(), BCrypt.gensalt(12));

        User user = User.builder()
                .id(UUID.randomUUID())
                .phoneNumber(registerDTO.getPhoneNumber())
                .email(registerDTO.getEmail())
                .password(passwordHashed)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);

        Student newStudent = Student.builder()
                .studentId(String.valueOf(UUID.randomUUID()))
                .user(user)
                .createdAt(LocalDateTime.now())
                .currentSchool(registerDTO.getCurrentSchool())
                .build();
        studentRepository.save(newStudent);
        return newStudent;
    }

    @Override
    public List<Student> listStudents() {
        return null;
    }

    @Override
    public Student getStudentInformation(String id) {
        return null;
    }

    @Override
    public List<Student> listStudentsByClass() {
        return null;
    }

    boolean validateRegisterRequest(StudentRegisterDTO registerDTO){
        return !Objects.isNull(registerDTO.getPhoneNumber());
    }
}
