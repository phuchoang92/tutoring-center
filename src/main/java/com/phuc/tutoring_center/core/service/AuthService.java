package com.phuc.tutoring_center.core.service;

import com.phuc.tutoring_center.core.domain.dto.request.LoginRequest;
import com.phuc.tutoring_center.core.domain.dto.response.JwtResponse;

public interface AuthService {
    JwtResponse signIn(LoginRequest loginRequest);
}
