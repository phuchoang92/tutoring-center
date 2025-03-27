package com.phuc.tutoring_center.core.service;

import com.phuc.tutoring_center.core.dto.request.StudentRegisterDTO;
import com.phuc.tutoring_center.core.entity.Student;

import java.util.List;

public interface StudentService {
    Student registerStudent(StudentRegisterDTO registerDTO);
    List<Student> listStudents();
    Student getStudentInformation(String id);
    List<Student> listStudentsByClass();
}
