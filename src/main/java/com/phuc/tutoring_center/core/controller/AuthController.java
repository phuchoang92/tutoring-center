package com.phuc.tutoring_center.core.controller;

import com.phuc.tutoring_center.core.dto.request.LoginRequest;
import com.phuc.tutoring_center.core.dto.response.JwtResponse;
import com.phuc.tutoring_center.core.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
       return ResponseEntity.ok(authService.signIn(loginRequest));
    }
}
