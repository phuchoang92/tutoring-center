package com.phuc.tutoring_center.core.auth;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;

@Service
public class JwtService {
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.accessToken.expirationMs}")
    private long accessTokenExpirationMs;

    @Value("${app.jwt.refreshToken.expirationMs}")
    private long refreshTokenExpirationMs;

    @Value("${app.jwt.issuer}")
    private String issuer;

    private Key key;

    @PostConstruct
    public void init(){
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
}
