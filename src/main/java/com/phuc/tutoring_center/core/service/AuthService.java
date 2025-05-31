package com.phuc.tutoring_center.core.service;

import com.phuc.tutoring_center.core.domain.dto.request.LoginRequest;
import com.phuc.tutoring_center.core.domain.dto.request.StudentRegisterDTO;
import com.phuc.tutoring_center.core.domain.dto.request.TeacherRegisterDTO;
import com.phuc.tutoring_center.core.domain.dto.response.JwtResponse;
import jakarta.validation.Valid;

public interface AuthService {
    JwtResponse signIn(@Valid LoginRequest loginRequest);

    Object registerStudent(@Valid StudentRegisterDTO registerDTO);
    Object registerTeacher(@Valid TeacherRegisterDTO registerDTO);
}
