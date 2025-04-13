package com.phuc.tutoring_center.core.repository;

import com.phuc.tutoring_center.core.domain.entity.RefreshToken;
import com.phuc.tutoring_center.core.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Transactional
    void deleteByUser(User user);
}