package com.phuc.tutoring_center.core.repository;

import com.phuc.tutoring_center.core.domain.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserInfoRepository extends JpaRepository<UserInfo, UUID> {
}