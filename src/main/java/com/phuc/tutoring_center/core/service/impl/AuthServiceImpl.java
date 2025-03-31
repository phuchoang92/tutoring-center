package com.phuc.tutoring_center.core.service.impl;

import com.phuc.tutoring_center.core.dto.request.LoginRequest;
import com.phuc.tutoring_center.core.dto.response.JwtResponse;
import com.phuc.tutoring_center.core.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public JwtResponse signIn(LoginRequest loginRequest) {
        return null;
    }
}
