package com.phuc.tutoring_center.core.controller;

import com.phuc.tutoring_center.core.domain.dto.request.LoginRequest;
import com.phuc.tutoring_center.core.domain.dto.request.StudentRegisterDTO;
import com.phuc.tutoring_center.core.domain.dto.request.TeacherRegisterDTO;
import com.phuc.tutoring_center.core.domain.dto.response.JwtResponse;
import com.phuc.tutoring_center.core.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.debug("Received login request for user: {}", loginRequest.getEmail());
        JwtResponse response = authService.signIn(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody StudentRegisterDTO registerRequest) {
        log.debug("Received student registration request for email: {}", registerRequest.getEmail());
        return ResponseEntity.ok(authService.registerStudent(registerRequest));
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<?> registerTeacher(@Valid @RequestBody TeacherRegisterDTO registerRequest) {
        log.debug("Received teacher registration request for email: {}", registerRequest.getEmail());
        return ResponseEntity.ok(authService.registerTeacher(registerRequest));
    }

    @GetMapping("/session-expired")
    public ResponseEntity<?> handleSessionExpired() {
        log.debug("Session expired request received");
        return ResponseEntity.status(401).body("Session expired. Please login again.");
    }
}
