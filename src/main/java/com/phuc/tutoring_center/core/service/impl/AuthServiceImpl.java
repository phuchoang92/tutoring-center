package com.phuc.tutoring_center.core.service.impl;

import com.phuc.tutoring_center.core.auth.JwtService;
import com.phuc.tutoring_center.core.auth.UserService;
import com.phuc.tutoring_center.core.domain.dto.request.LoginRequest;
import com.phuc.tutoring_center.core.domain.dto.request.StudentRegisterDTO;
import com.phuc.tutoring_center.core.domain.dto.request.TeacherRegisterDTO;
import com.phuc.tutoring_center.core.domain.dto.response.JwtResponse;
import com.phuc.tutoring_center.core.domain.entity.RefreshToken;
import com.phuc.tutoring_center.core.domain.entity.User;
import com.phuc.tutoring_center.core.exception.BusinessException;
import com.phuc.tutoring_center.core.service.AuthService;
import com.phuc.tutoring_center.core.service.StudentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
    @Transactional
    public JwtResponse signIn(@Valid LoginRequest loginRequest) {
        try {
            log.debug("Attempting to authenticate user: {}", loginRequest.getEmail());
            
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();

            if (!user.isEnabled()) {
                throw new BusinessException("Account is disabled", 401, "ACCOUNT_DISABLED");
            }

            if (!user.isAccountNonLocked()) {
                throw new BusinessException("Account is locked", 401, "ACCOUNT_LOCKED");
            }

            userService.updateLastLogin(user.getEmail());
            String accessToken = jwtService.generateTokenAccess(user);
            RefreshToken refreshToken = jwtService.generateRefreshToken(user);

            log.info("User {} successfully authenticated", user.getEmail());

            return JwtResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .build();
        } catch (BadCredentialsException e) {
            log.warn("Authentication failed for user: {}", loginRequest.getEmail());
            throw new BusinessException("Invalid email or password", 401, "INVALID_CREDENTIALS");
        } catch (Exception e) {
            log.error("Error during authentication for user: {}", loginRequest.getEmail(), e);
            throw new BusinessException("Authentication failed", 500, "INTERNAL_ERROR");
        }
    }

    @Override
    @Transactional
    public Object registerStudent(StudentRegisterDTO registerDTO) {
        return studentService.registerStudent(registerDTO);
    }

    @Override
    @Transactional
    public Object registerTeacher(TeacherRegisterDTO registerDTO) {
        return null;
    }
}
