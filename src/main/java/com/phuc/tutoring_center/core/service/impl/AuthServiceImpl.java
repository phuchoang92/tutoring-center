package com.phuc.tutoring_center.core.service.impl;

import com.phuc.tutoring_center.core.auth.JwtService;
import com.phuc.tutoring_center.core.auth.UserService;
import com.phuc.tutoring_center.core.domain.dto.request.LoginRequest;
import com.phuc.tutoring_center.core.domain.dto.request.StudentRegisterDTO;
import com.phuc.tutoring_center.core.domain.dto.request.TeacherRegisterDTO;
import com.phuc.tutoring_center.core.domain.dto.response.JwtResponse;
import com.phuc.tutoring_center.core.domain.entity.RefreshToken;
import com.phuc.tutoring_center.core.domain.entity.User;
import com.phuc.tutoring_center.core.service.AuthService;
import com.phuc.tutoring_center.core.service.StudentService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final StudentService studentService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserService userService, StudentService studentService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.studentService = studentService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Object signIn(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        userService.updateLastLogin(user.getEmail());
        String accessToken = jwtService.generateTokenAccess(user);
        RefreshToken refreshToken = jwtService.generateRefreshToken(user);

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public Object registerStudent(StudentRegisterDTO registerDTO) {
        return studentService.registerStudent(registerDTO);
    }

    @Override
    public Object registerTeacher(TeacherRegisterDTO registerDTO) {
        return null;
    }
}
