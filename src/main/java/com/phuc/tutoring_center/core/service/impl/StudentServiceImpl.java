package com.phuc.tutoring_center.core.service.impl;

import com.phuc.tutoring_center.core.dto.request.StudentRegisterDTO;
import com.phuc.tutoring_center.core.entity.Student;
import com.phuc.tutoring_center.core.repository.StudentRepository;
import com.phuc.tutoring_center.core.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    @Override
    public Student registerStudent(StudentRegisterDTO registerDTO) {
        validateRegisterRequest(registerDTO);
        Student student = studentRepository.findByPhoneNumber(registerDTO.getPhoneNumber())
                .orElse(null);
        if (!Objects.isNull(student)){
            throw new RuntimeException("This phone number has existed");
        }
        Student newStudent = Student.builder()
                .studentId(String.valueOf(UUID.randomUUID()))
                .age(registerDTO.getAge())
                .name(registerDTO.getName())
                .address(registerDTO.getAddress())
                .registrationDate(LocalDate.now())
                .phoneNumber(registerDTO.getPhoneNumber())
                .dateOfBirth(registerDTO.getDateOfBirth())
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
