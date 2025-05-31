package com.phuc.tutoring_center.core.auth;

import com.phuc.tutoring_center.core.domain.entity.User;
import com.phuc.tutoring_center.core.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
                // Clear the security context
                SecurityContextHolder.clearContext();
                
                // Invalidate refresh token if exists
                if (authentication != null && authentication.getPrincipal() instanceof User) {
                    User user = (User) authentication.getPrincipal();
                    refreshTokenRepository.deleteByUser(user);
                    log.debug("Successfully invalidated refresh token for user: {}", user.getEmail());
                }
            }
        } catch (Exception e) {
            log.error("Error during logout process: {}", e.getMessage());
            // Don't throw exception as this is a cleanup operation
        }
    }
} 