package com.phuc.tutoring_center.core.auth;

import com.phuc.tutoring_center.core.domain.entity.RefreshToken;
import com.phuc.tutoring_center.core.domain.entity.User;
import com.phuc.tutoring_center.core.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
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
    private final RefreshTokenRepository refreshTokenRepository;

    @PostConstruct
    public void init() {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
            this.key = Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            log.error("Error initializing JWT key: {}", e.getMessage());
            throw new RuntimeException("Error initializing JWT key", e);
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Error extracting claims from token: {}", e.getMessage());
            throw new RuntimeException("Error extracting claims from token", e);
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            log.error("Error validating token: {}", e.getMessage());
            return false;
        }
    }

    public String generateTokenAccess(UserDetails userDetails) {
        try {
            Map<String, Object> claims = new HashMap<>();

            String roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            claims.put("roles", roles);

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationMs))
                    .setIssuer(issuer)
                    .setId(UUID.randomUUID().toString())
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        } catch (Exception e) {
            log.error("Error generating access token: {}", e.getMessage());
            throw new RuntimeException("Error generating access token", e);
        }
    }

    @Transactional
    public RefreshToken generateRefreshToken(User user) {
        try {
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setUser(user);
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpirationMs));

            refreshTokenRepository.deleteByUser(user);
            return refreshTokenRepository.save(refreshToken);
        } catch (Exception e) {
            log.error("Error generating refresh token: {}", e.getMessage());
            throw new RuntimeException("Error generating refresh token", e);
        }
    }
}
