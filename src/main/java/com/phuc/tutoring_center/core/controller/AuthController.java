package com.phuc.tutoring_center.core.controller;

import com.phuc.tutoring_center.core.domain.dto.request.LoginRequest;
import com.phuc.tutoring_center.core.domain.dto.request.StudentRegisterDTO;
import com.phuc.tutoring_center.core.domain.dto.request.TeacherRegisterDTO;
import com.phuc.tutoring_center.core.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
       return ResponseEntity.ok(authService.signIn(loginRequest));
    }

    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@RequestBody StudentRegisterDTO registerRequest){
        return ResponseEntity.ok(authService.registerStudent(registerRequest));
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<?> registerTeacher(@RequestBody TeacherRegisterDTO registerRequest){
        return ResponseEntity.ok(authService.registerTeacher(registerRequest));
    }
}
