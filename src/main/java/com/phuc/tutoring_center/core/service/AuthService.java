package com.phuc.tutoring_center.core.service;

import com.phuc.tutoring_center.core.domain.dto.request.LoginRequest;
import com.phuc.tutoring_center.core.domain.dto.request.StudentRegisterDTO;
import com.phuc.tutoring_center.core.domain.dto.request.TeacherRegisterDTO;

public interface AuthService {
    Object signIn(LoginRequest loginRequest);

    Object registerStudent(StudentRegisterDTO registerDTO);
    Object registerTeacher(TeacherRegisterDTO registerDTO);
}
