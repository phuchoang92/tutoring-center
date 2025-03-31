package com.phuc.tutoring_center.core.service;

import com.phuc.tutoring_center.core.dto.request.LoginRequest;
import com.phuc.tutoring_center.core.dto.response.JwtResponse;

public interface AuthService {
    JwtResponse signIn(LoginRequest loginRequest);
}
